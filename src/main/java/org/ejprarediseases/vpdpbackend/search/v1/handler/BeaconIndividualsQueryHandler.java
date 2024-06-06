package org.ejprarediseases.vpdpbackend.search.v1.handler;

import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchRequest;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Sex;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.BeaconRequestBody;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.ontology_filter.BeaconRequestBodyOntologyFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyMetaSection;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyQuerySection;
import org.ejprarediseases.vpdpbackend.utils.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconFilterHandler.*;

public class BeaconIndividualsQueryHandler {

    static Logger logger = LoggerFactory.getLogger(BeaconIndividualsQueryHandler.class);


    /**
     * Retrieves a response from the specified beacon resource using the provided request body and authorization key.
     *
     * @param resource The resource to query.
     * @param requestBody The request body for the query.
     * @param authKey The authorization key.
     * @return The response string from the resource.
     */
    public static String getResponse(Resource resource, BeaconRequestBody requestBody, String authKey) {
        String response;
        try {
            WebClient client = WebClient.create();
            response = client.post()
                    .uri( resource.getResourceAddress())
                    .bodyValue(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("auth-key", authKey)
                    .header("Authorization", UserHandler.getBearerToken())
                    .retrieve()
                     .onStatus(httpStatus -> httpStatus.value() == 403,
                             error -> Mono.error(new RuntimeException("error Body")))
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            logger.error("authKey for resource: " + resource.getResourceName() + " is not defined." + e);
            return null;
        }
        return response;
    }

    /**
     * Builds and returns the meta section of a Beacon query request body.
     *
     * @return The constructed BeaconRequestBodyMetaSection.
     */
    public static BeaconRequestBodyMetaSection buildMetaSection() {
        BeaconRequestBodyMetaSection metaSection = new BeaconRequestBodyMetaSection();
        metaSection.setApiVersion("v0.2");
        return metaSection;
    }

    /**
     * Builds and returns the query section of a Beacon query request body.
     *
     * @param searchRequest The search request containing filter criteria.
     * @return The constructed BeaconRequestBodyQuerySection.
     */
    private static BeaconRequestBodyQuerySection buildQuerySection(SearchRequest searchRequest) {
        BeaconRequestBodyQuerySection querySection = new BeaconRequestBodyQuerySection();
        List<BeaconRequestBodyFilter> filters = new ArrayList<>();
        filters.add(buildDiseaseFilter(searchRequest.getDiseases()));
        if (searchRequest.getSexes() != null && !searchRequest.getSexes().isEmpty()) {
            filters.add(buildSexFilter(searchRequest.getSexes()));
        }
        if (searchRequest.getAgeThisYear() != null && searchRequest.getAgeThisYear().size() == 2) {
            filters.add(buildMinAgeFilter(searchRequest.getAgeThisYear().get(0)));
            filters.add(buildMaxAgeFilter(searchRequest.getAgeThisYear().get(1)));
        }
        if (searchRequest.getSymptomOnset() != null && searchRequest.getSymptomOnset().size() == 2) {
            filters.add(buildMinSymptomOnsetFilter(searchRequest.getSymptomOnset().get(0)));
            filters.add(buildMaxSymptomOnsetFilter(searchRequest.getSymptomOnset().get(1)));
        }
        if (searchRequest.getAgeAtDiagnosis() != null && searchRequest.getAgeAtDiagnosis().size() == 2) {
            filters.add(buildMinAgeAtDiagnosisFilter(searchRequest.getAgeAtDiagnosis().get(0)));
            filters.add(buildMaxAgeAtDiagnosisFilter(searchRequest.getAgeAtDiagnosis().get(1)));
        }
        querySection.setFilters(filters);
        return querySection;
    }

    /**
     * Converts a SearchRequest into a BeaconRequestBody.
     *
     * @param searchRequest The search request containing filter criteria.
     * @return The constructed BeaconRequestBody.
     */
    public static BeaconRequestBody convertToBeaconRequestBody(SearchRequest searchRequest) {
        BeaconRequestBody requestBody = new BeaconRequestBody();
        requestBody.setMeta(buildMetaSection());
        requestBody.setQuery(buildQuerySection(searchRequest));
        return requestBody;
    }

    /**
     * Builds and returns a default query section for Beacon query request body.
     *
     * @return The constructed BeaconRequestBodyQuerySection.
     */
    private static BeaconRequestBodyQuerySection getDefaultQuerySection() {
        BeaconRequestBodyQuerySection querySection = new BeaconRequestBodyQuerySection();
        List<BeaconRequestBodyFilter> filters = new ArrayList<>();
        BeaconRequestBodyOntologyFilter filter = new BeaconRequestBodyOntologyFilter();
        List<String> ids = new ArrayList<>();
        ids.add("ordo:Orphanet_730");
        ids.add("ordo:Orphanet_635");
        filter.setId(ids);
        filters.add(filter);
        //filters.add(buildSexFilter(Arrays.asList(Sex.values())));
        filters.add(buildMinAgeFilter(0));
        filters.add(buildMaxAgeFilter(100));
        filters.add(buildMinSymptomOnsetFilter(0));
        filters.add(buildMaxSymptomOnsetFilter(100));
        filters.add(buildMinAgeAtDiagnosisFilter(0));
        filters.add(buildMaxAgeAtDiagnosisFilter(100));
        querySection.setFilters(filters);
        return querySection;
    }

    /**
     * Builds and returns a default request body for Beacon query.
     *
     * @return The constructed BeaconRequestBody.
     */
    public static BeaconRequestBody getDefaultRequestBody() {
        BeaconRequestBody requestBody = new BeaconRequestBody();
        requestBody.setMeta(buildMetaSection());
        requestBody.setQuery(getDefaultQuerySection());
        return requestBody;
    }


}
