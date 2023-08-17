package org.ejprarediseases.vpdpbackend.resource_monitoring.v1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource.v1.ResourceService;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Monitor;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.Period;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.ResourceMonitoringSummary;
import org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.enums.HttpStatusCategory;
import org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconCatalogQueryHandler;
import org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconIndividualsQueryHandler;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.BeaconRequestBody;
import org.ejprarediseases.vpdpbackend.utils.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.ejprarediseases.vpdpbackend.resource.v1.model.QueryType.BEACON_CATALOG;
import static org.ejprarediseases.vpdpbackend.resource.v1.model.QueryType.BEACON_INDIVIDUALS;
import static org.ejprarediseases.vpdpbackend.resource_monitoring.v1.model.enums.HttpStatusCategory.*;

@Service
@RequiredArgsConstructor
public class ResourceMonitoringService {

    private final ResourceService resourceService;

    private final ResourceMonitoringRepository repository;

    static Logger logger = LoggerFactory.getLogger(StringConverter.class);

    /**
     * Initializes resource monitoring upon application start and schedules regular monitoring at defined intervals.
     */
    @PostConstruct
    @Scheduled(timeUnit = TimeUnit.MINUTES,
            fixedRateString = "${application.monitoring.scheduledMonitoringRateInMinutes}")
    public void monitorAllResources() {
        try {
            List<Resource> resources = resourceService.getAllResources();
            for (Resource resource : resources) {
                if (resource.isQueryable()) {
                    monitorBeaconResource(resource);
                }
            }
        } catch (IOException io) {
            logger.error("Failed to load list of resources with cause: " + io.getMessage());
        }
    }

    /**
     * Asynchronously monitors a Beacon resource's availability and performance.
     *
     * @param resource The resource to monitor.
     */
    @Async
    void monitorBeaconResource(Resource resource) {
        String authKey = resourceService.getResourceAuthKeyById(resource.getId());
        BeaconRequestBody defaultRequestBody = null;
        if (resource.getQueryType().contains(BEACON_INDIVIDUALS)) {
            defaultRequestBody = BeaconIndividualsQueryHandler.getDefaultRequestBody();
        } else if (resource.getQueryType().contains(BEACON_CATALOG)) {
            defaultRequestBody = BeaconCatalogQueryHandler.getDefaultRequestBody();
        }
        long startTime = System.currentTimeMillis();
        Monitor monitor = new Monitor();
        monitor.setResourceId(resource.getId());
        WebClient client = WebClient.create();
        Mono<String> response = client.post()
                .uri( resource.getResourceAddress())
                .bodyValue(defaultRequestBody)
                .accept(MediaType.APPLICATION_JSON)
                .header("auth-key", authKey)
                .exchangeToMono(rs -> {
                    monitor.setResponseStatusCode(rs.statusCode().value());
                    return rs.bodyToMono(String.class);
                });
        monitor.setResponseBody(response.block());
        long endTime = System.currentTimeMillis();
        monitor.setResponseTime(endTime - startTime);
        repository.save(monitor);
    }

    /**
     * Retrieves a summary of resource monitoring information for a specified period.
     *
     * @param resourceId  The ID of the resource to summarize.
     * @param period The period value & unit (e.g. DAYS, etc.) for which to generate the summary.
     * @return A summary of resource monitoring information.
     */
    public ResourceMonitoringSummary getResourceMonitoringSummary(String resourceId, Period period) {
        ResourceMonitoringSummary summary = new ResourceMonitoringSummary();
        List<Monitor> monitors = getMonitorsByResourceId(resourceId, period);
        if (monitors.size() < 1) {
            return new ResourceMonitoringSummary(resourceId, new Period(), 0, 0, 0, 0, 0, 0, 0);
        }
        summary.setResourceId(resourceId);
        summary.setPeriod(period);
        summary.setNumberOfTests(monitors.size());
        summary.setAverageResponseTimeInMilliSeconds(getAverageResponseTime(monitors));
        List<HttpStatusCategory> monitorsHttpStatusCategories = convertMonitorsToHttpStatusCategories(monitors);
        summary.setNumberOfInformationalResponses(
                countCategoryOccurrences(INFORMATIONAL_RESPONSE, monitorsHttpStatusCategories));
        summary.setNumberOfSuccessfulResponses(
                countCategoryOccurrences(SUCCESSFUL_RESPONSE, monitorsHttpStatusCategories));
        summary.setNumberOfRedirectionResponses(
                countCategoryOccurrences(REDIRECTION_RESPONSE, monitorsHttpStatusCategories));
        summary.setNumberOfClientErrorResponses(
                countCategoryOccurrences(CLIENT_ERROR_RESPONSE, monitorsHttpStatusCategories));
        summary.setNumberOfServerErrorResponses(
                countCategoryOccurrences(SERVER_ERROR_RESPONSE, monitorsHttpStatusCategories));
        return summary;
    }

    /**
     * Calculate the start timestamp for a given period.
     *
     * This method takes a Period object and calculates the start timestamp by subtracting the period's value
     * and unit from the current time in the "Europe/Berlin" time zone.
     *
     * @param period The period for which the start timestamp needs to be calculated.
     * @return The calculated start timestamp.
     */
    public Timestamp getPeriodStartTimestamp(Period period) {
        return Timestamp.valueOf(ZonedDateTime.now(
                ZoneId.of("Europe/Berlin")).minus(period.getValue(), period.getUnit()).toLocalDateTime());
    }

    /**
     * Get the current timestamp.
     *
     * This method retrieves the current timestamp in the "Europe/Berlin" time zone.
     *
     * @return The current timestamp.
     */
    public Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("Europe/Berlin")).toLocalDateTime());
    }

    /**
     * Get HTTP statuses from a list of monitors.
     *
     * This method takes a list of Monitor objects and retrieves their corresponding HTTP statuses. The statuses
     * are collected and returned as a list.
     *
     * @param monitors The list of monitors from which to retrieve HTTP statuses.
     * @return A list of HTTP statuses.
     */
    public List<HttpStatus> getMonitorsStatuses(List<Monitor> monitors) {
        return monitors.stream().map(monitor -> HttpStatus.valueOf(monitor.getResponseStatusCode())).toList();
    }

    /**
     * Filter monitors according to accepted HTTP categories.
     *
     * This method takes a list of Monitor objects and a list of accepted HTTP categories. It filters the monitors
     * based on whether their corresponding HTTP status codes belong to the accepted categories.
     *
     * @param monitors The list of monitors to be filtered.
     * @param acceptedCategories The list of accepted HTTP categories for filtering.
     * @return A list of filtered monitors.
     */
    public List<Monitor> filterMonitorsAccordingToHttpCategories(
            List<Monitor> monitors, List<HttpStatusCategory> acceptedCategories) {
        return monitors.stream()
                .filter(monitor -> acceptedCategories.contains(monitorToHttpStatusCategory(monitor))).toList();
    }

    /**
     * Retrieve monitors for a specific resource within a specified period.
     *
     * This method retrieves a list of monitors associated with a given resource ID, where the monitors' timestamps
     * fall within the specified period. The start and end timestamps of the period are calculated using the provided
     * period object and the current time in the "Europe/Berlin" time zone.
     *
     * @param resourceId The ID of the resource for which monitors are to be retrieved.
     * @param period The period for which monitors are to be retrieved.
     * @return A list of monitors associated with the resource within the specified period.
     */
    public List<Monitor> getMonitorsByResourceId(String resourceId, Period period) {
        return repository.findAllByResourceIdEqualsAndTimestampBetween(
                resourceId, getPeriodStartTimestamp(period), getCurrentTimestamp());
    }

    /**
     * Converts a list of Monitor instances into a list of corresponding HttpStatusCategory instances.
     *
     * @param monitors The list of Monitor instances to convert.
     * @return A list of HttpStatusCategory instances corresponding to the provided monitors.
     */
    private List<HttpStatusCategory> convertMonitorsToHttpStatusCategories(List<Monitor> monitors) {
        return monitors.stream().map(
                ResourceMonitoringService::monitorToHttpStatusCategory).toList();
    }

    /**
     * Maps a Monitor instance to its corresponding HttpStatusCategory.
     *
     * @param monitor The Monitor instance to map.
     * @return The HttpStatusCategory corresponding to the provided Monitor instance.
     */
    private static HttpStatusCategory monitorToHttpStatusCategory(Monitor monitor) {
        int statusCodeCategoryIndicator =
                Integer.parseInt(String.valueOf(monitor.getResponseStatusCode()).substring(0, 1));
        return switch (statusCodeCategoryIndicator) {
            case 1 -> INFORMATIONAL_RESPONSE;
            case 2 -> SUCCESSFUL_RESPONSE;
            case 3 -> REDIRECTION_RESPONSE;
            case 4 -> CLIENT_ERROR_RESPONSE;
            case 5 -> SERVER_ERROR_RESPONSE;
            default -> throw new IllegalStateException("Unexpected value: " + statusCodeCategoryIndicator);
        };
    }

    /**
     * Counts the occurrences of a specific HttpStatusCategory in a list of HttpStatusCategory instances.
     *
     * @param targetCategory The HttpStatusCategory to count occurrences for.
     * @param list           The list of HttpStatusCategory instances to search in.
     * @return The count of occurrences of the target HttpStatusCategory.
     */
    private int countCategoryOccurrences(HttpStatusCategory targetCategory, List<HttpStatusCategory> list) {
        return Collections.frequency(list, targetCategory);
    }

    /**
     * Calculates the average response time from a list of Monitor instances.
     *
     * @param monitors The list of Monitor instances to calculate the average response time from.
     * @return The average response time in milliseconds.
     */
    private int getAverageResponseTime(List<Monitor> monitors) {
        return (int) (monitors.stream().mapToLong(Monitor::getResponseTime).sum() / monitors.size());
    }

}
