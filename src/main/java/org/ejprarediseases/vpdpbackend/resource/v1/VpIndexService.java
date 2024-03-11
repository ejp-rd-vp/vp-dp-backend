package org.ejprarediseases.vpdpbackend.resource.v1;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.ejprarediseases.vpdpbackend.resource.v1.model.QueryType;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VpIndexService {

    private static RestTemplate restTemplate = new RestTemplate();

    @Value("${application.vpIndexAuthKeys}")
    private String vpIndexAuthKeys;

    private String vpIndexUrl="https://graphdb.ejprd.semlab-leiden.nl/repositories/vp-index";
    private String vpIndexUmid = "D100CB7A-040D-A506-A705-44EEA1D0E378";



    /**
     * Retrieves a list of all resource from the VP Index via sparql queries.
     *
     * @return A list of resources listed in the VP Index.
     * @throws Exception
     */
    public List<Resource> getAllResourceByVpIndex() throws IOException {
        return transformToResourceList(fetchVpIndex());
    }

    //TODO: delete, just implemented for testing



    private JsonArray fetchVpIndex() throws IOException {

        ClassPathResource queryfile = new ClassPathResource("static/query.sparql");
        String query = new BufferedReader(new InputStreamReader(queryfile.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/sparql-results+json");
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("query",query);

        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                vpIndexUrl + "?umid="+ vpIndexUmid + "&auth=" + vpIndexAuthKeys ,
                HttpMethod.POST, entity, String.class);

        JsonObject jsonObject = new Gson().fromJson(responseEntity.getBody(), JsonObject.class);

        return jsonObject.getAsJsonObject("results").getAsJsonArray("bindings");
    }


    private List<Resource> transformToResourceList (JsonArray resources) throws IOException {
        List<Resource> resourceList = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++){
            JsonElement element = resources.get(i);
            Resource resource = new Resource();

            resource.setId(String.valueOf(i));

            if (element.getAsJsonObject().getAsJsonObject("resourceName")!=null) {
                resource.setResourceName(element.getAsJsonObject().getAsJsonObject("resourceName").
                        get("value").getAsString());
            }else{
                resource.setResourceName("");
            }

            if (element.getAsJsonObject().getAsJsonObject("resourceDescription")!=null) {
                resource.setResourceDescription(element.getAsJsonObject().getAsJsonObject("resourceDescription").
                        get("value").getAsString());
            }else{
                resource.setResourceDescription("");
            }

            if (element.getAsJsonObject().getAsJsonObject("resourceHomePage")!=null) {
                resource.setResourceHomePage(element.getAsJsonObject().getAsJsonObject("resourceHomePage").
                        get("value").getAsString());
            }else{
                resource.setResourceHomePage("");
            }

            if (element.getAsJsonObject().getAsJsonObject("logo")!=null) {
                resource.setLogo(element.getAsJsonObject().getAsJsonObject("logo").
                        get("value").getAsString());
            }else{
                resource.setLogo("");
            }

            if (element.getAsJsonObject().getAsJsonObject("resourceType")!=null) {
                List<ResourceType> resourceTypes = new ArrayList<>();
                switch(element.getAsJsonObject().getAsJsonObject("resourceType").
                        get("value").getAsString()){
                    case "PATIENT_REGISTRY":
                        resourceTypes.add(ResourceType.PATIENT_REGISTRY);
                        break;
                    case "BIO_BANK":
                        resourceTypes.add(ResourceType.BIO_BANK);
                        break;
                    case "DATASET":
                        resourceTypes.add(ResourceType.DATASET);
                        break;
                    case "GUIDELINE":
                        resourceTypes.add(ResourceType.GUIDELINE);
                        break;
                    case "CATALOG":
                        resourceTypes.add(ResourceType.CATALOG);
                        break;
                    default:
                        throw new IOException(" Invalid ResourceType for " + resource.getResourceName() + "!");
                }

                resource.setResourceType(resourceTypes);

            }else{
                List<ResourceType> resourceTypes = new ArrayList<>();
                resourceTypes.add(ResourceType.DATASET);
                resource.setResourceType(resourceTypes);
            }

            if (element.getAsJsonObject().getAsJsonObject("vpConnectionType")!=null) {
                String vpConnectionType = element.getAsJsonObject().getAsJsonObject("vpConnectionType").
                        get("value").getAsString();
                if (vpConnectionType.split("/")[vpConnectionType.split("/").length-1].equals("VPContentDiscovery")) {
                    resource.setQueryable(true);
                }else{
                    resource.setQueryable(false);
                }
            }

            //TODO: convert String to Timestamp
            /*if (element.getAsJsonObject().getAsJsonObject("created")!=null) {
                resource.setCreated(element.getAsJsonObject().getAsJsonObject("created").
                        get("value").getAsString());
            }

            if (element.getAsJsonObject().getAsJsonObject("updated")!=null) {
                resource.setUpdated(element.getAsJsonObject().getAsJsonObject("updated").
                        get("value").getAsString());
            }*/

            //TODO error1: queryable true, aber keine resourceAddress vorhanden (null)
            //TODO error2: QueryType is null

            //TODO
            String email;
            String resourceAddress;
            QueryType queryType;

            //set default
            resource.setCreated(new Timestamp(0));
            resource.setUpdated(new Timestamp(0));
            resource.setEmail("");
            if (resource.isQueryable()){
                if(resource.getResourceName().equals("RD-Connect GPAP")){
                    resource.setResourceAddress("https://platform.rd-connect.eu/beacon2/api/individuals");
                    List<QueryType> queryTypes = new ArrayList<>();
                    queryTypes.add(QueryType.BEACON_INDIVIDUALS);
                    resource.setQueryType(queryTypes);
                }else{
                    resource.setResourceAddress("");
                    resource.setQueryable(false);
                }
            }else{
                resource.setResourceAddress("");
            }

            resource.setSpecsURL("");
            /*resource.setQueryType();
            resource.setResourceContentType();
            resource.setTheme();*/



            resourceList.add(resource);
        }
        return resourceList;
    }

}
