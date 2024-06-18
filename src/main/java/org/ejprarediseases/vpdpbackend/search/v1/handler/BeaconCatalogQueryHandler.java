package org.ejprarediseases.vpdpbackend.search.v1.handler;

import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchRequest;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.BeaconRequestBody;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.BeaconRequestBodyFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.ontology_filter.BeaconRequestBodyOntologyFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyMetaSection;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.sections.BeaconRequestBodyQuerySection;
import org.ejprarediseases.vpdpbackend.utils.UserHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconFilterHandler.*;

public class BeaconCatalogQueryHandler {

    /**
     * Sends a query to the specified resource's address with the provided request body and retrieves the response.
     *
     * @param resource   The resource to query.
     * @param requestBody The request body for the query.
     * @return The response received from the query.
     */
    public static String getResponse(Resource resource, BeaconRequestBody requestBody) {
        WebClient client = WebClient.create();
        String responseSpec = client.post()
                .uri( resource.getResourceAddress())
                .bodyValue(requestBody)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", UserHandler.getBearerToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return responseSpec;
    }

    /**
     * Builds the meta section of the Beacon request body.
     *
     * @return The constructed BeaconRequestBodyMetaSection.
     */
    private static BeaconRequestBodyMetaSection buildMetaSection() {
        BeaconRequestBodyMetaSection metaSection = new BeaconRequestBodyMetaSection();
        metaSection.setApiVersion("v0.2");
        return metaSection;
    }

    /**
     * Builds the query section of the Beacon request body based on the provided SearchRequest.
     *
     * @param searchRequest The search parameters.
     * @return The constructed BeaconRequestBodyQuerySection.
     */
    private static BeaconRequestBodyQuerySection buildQuerySection(SearchRequest searchRequest) {
        BeaconRequestBodyQuerySection querySection = new BeaconRequestBodyQuerySection();
        List<BeaconRequestBodyFilter> filters = new ArrayList<>();
        filters.add(buildDiseaseFilter(searchRequest.getDiseases()));
        if(searchRequest.getResourceTypes() != null && !searchRequest.getResourceTypes().isEmpty()) {
            filters.add(buildResourceTypesFilter(searchRequest.getResourceTypes()));
        }
        if(searchRequest.getCountries() != null && !searchRequest.getCountries().isEmpty()) {
            filters.add(buildCountryFilter(searchRequest.getCountries()));
        }
        querySection.setFilters(filters);
        return querySection;
    }

    /**
     * Converts a SearchRequest to a BeaconRequestBody.
     *
     * @param searchRequest The search parameters.
     * @return The constructed BeaconRequestBody.
     */
    public static BeaconRequestBody convertToBeaconRequestBody(SearchRequest searchRequest) {
        BeaconRequestBody requestBody = new BeaconRequestBody();
        requestBody.setMeta(buildMetaSection());
        requestBody.setQuery(buildQuerySection(searchRequest));
        return requestBody;
    }

    /**
     * Builds the default query section of the Beacon request body.
     *
     * @return The constructed default BeaconRequestBodyQuerySection.
     */
    private static BeaconRequestBodyQuerySection getDefaultQuerySection() {
        BeaconRequestBodyQuerySection querySection = new BeaconRequestBodyQuerySection();
        List<BeaconRequestBodyFilter> filters = new ArrayList<>();
        BeaconRequestBodyOntologyFilter filter = new BeaconRequestBodyOntologyFilter();
        filter.setId(Collections.singletonList("ordo:Orphanet_730"));
        filters.add(filter);
        filters.add(buildResourceTypesFilter(Arrays.asList(ResourceType.values())));
        filters.add(buildCountryFilter(Arrays.asList(Country.values())));
        querySection.setFilters(filters);
        return querySection;
    }

    /**
     * Gets the default request body for the Beacon query.
     *
     * @return The default BeaconRequestBody.
     */
    public static BeaconRequestBody getDefaultRequestBody() {
        BeaconRequestBody requestBody = new BeaconRequestBody();
        requestBody.setMeta(buildMetaSection());
        requestBody.setQuery(getDefaultQuerySection());
        return requestBody;
    }

}
