package org.ejprarediseases.vpdpbackend.search.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.ejprarediseases.vpdpbackend.resource.v1.ResourceService;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconCatalogQueryHandler;
import org.ejprarediseases.vpdpbackend.search.v1.handler.BeaconIndividualsQueryHandler;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchRequest;
import org.ejprarediseases.vpdpbackend.search.v1.model.SearchResponse;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.BeaconRequestBody;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.BeaconResponseBody;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.CatalogResponseBody;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.response_body.IndividualsResponseBody;
import org.ejprarediseases.vpdpbackend.utils.ObjectIOHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.ejprarediseases.vpdpbackend.resource.v1.model.QueryType.BEACON_CATALOG;
import static org.ejprarediseases.vpdpbackend.resource.v1.model.QueryType.BEACON_INDIVIDUALS;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ResourceService resourceService;

    /**
     * Performs a search for diseases by OrphaCode using Beacon resources.
     *
     * @param searchRequest The search request containing filter criteria.
     * @return The search response containing the retrieved information.
     * @throws IOException If there is an I/O error.
     * @throws NoSuchElementException If the specified resource is not found.
     */
    public SearchResponse searchForDiseasesByOrphaCode(SearchRequest searchRequest)
            throws IOException, NoSuchElementException {
        Resource resource = resourceService.getResourceById(searchRequest.getResourceId());
        if (resource.getQueryType().contains(BEACON_INDIVIDUALS)) {
            String authKey = resourceService.getResourceAuthKeyById(searchRequest.getResourceId());
            return handleBeaconIndividualsQuery(searchRequest, resource, authKey);
        } else if (resource.getQueryType().contains(BEACON_CATALOG)) {
            return handleBeaconCatalogQuery(searchRequest, resource);
        }
        return new SearchResponse();
    }

    /**
     * Handles the search request for Beacon Individuals query and returns the corresponding search response.
     *
     * @param searchRequest The search request containing filter criteria.
     * @param resource The Beacon resource to query.
     * @param authKey The authentication key for accessing the resource.
     * @return The search response containing the retrieved information.
     * @throws JsonProcessingException If there is an error during JSON processing.
     */
    private SearchResponse handleBeaconIndividualsQuery(
            SearchRequest searchRequest, Resource resource, String authKey) throws JsonProcessingException {
        BeaconRequestBody requestBody =
                BeaconIndividualsQueryHandler.convertToBeaconRequestBody(searchRequest);
        String beaconResponseAsString = BeaconIndividualsQueryHandler.getResponse(resource, requestBody, authKey);
        if (beaconResponseAsString != null) {
            BeaconResponseBody response =
                    ObjectIOHandler.deserialize(beaconResponseAsString, IndividualsResponseBody.class);
            return beaconToSearchResponse(resource, response);
        }
        return null;
    }

    /**
     * Handles the search request for Beacon Catalog query and returns the corresponding search response.
     *
     * @param searchRequest The search request containing filter criteria.
     * @param resource The Beacon resource to query.
     * @return The search response containing the retrieved information.
     * @throws JsonProcessingException If there is an error during JSON processing.
     */
    private SearchResponse handleBeaconCatalogQuery(
            SearchRequest searchRequest, Resource resource) throws JsonProcessingException {
        BeaconRequestBody requestBody = BeaconCatalogQueryHandler.convertToBeaconRequestBody(searchRequest);
        String beaconResponseAsString = BeaconCatalogQueryHandler.getResponse(resource, requestBody);
        if (beaconResponseAsString != null) {
            BeaconResponseBody response =
                    ObjectIOHandler.deserialize(beaconResponseAsString, CatalogResponseBody.class);
            return beaconToSearchResponse(resource, response);
        }
        return new SearchResponse();
    }

    /**
     * Converts the Beacon query response to a SearchResponse.
     *
     * @param resource The Beacon resource.
     * @param beaconResponseBody The Beacon response body.
     * @return The converted SearchResponse.
     */
    private SearchResponse beaconToSearchResponse(Resource resource, BeaconResponseBody beaconResponseBody) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setResourceId(resource.getId());
        searchResponse.setResourceName(resource.getResourceName());
        searchResponse.setContent(beaconResponseBody);
        return searchResponse;
    }


}
