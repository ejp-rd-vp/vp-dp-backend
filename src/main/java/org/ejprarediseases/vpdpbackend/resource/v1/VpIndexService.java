package org.ejprarediseases.vpdpbackend.resource.v1;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VpIndexService {

    private static RestTemplate restTemplate = new RestTemplate();

    @Value("${application.vpIndexAuthKeys}")
    private String vpIndexAuthKeys;

    private final String vpIndexUrl="https://graphdb.ejprd.semlab-leiden.nl/repositories/vp-index";
    private String vpIndexUmid = "D100CB7A-040D-A506-A705-44EEA1D0E378";



    /**
     * Retrieves a list of all resource from the VP Index via sparql queries.
     *
     * @return A list of resources listed in the VP Index.
     * @throws Exception
     */
    public List<Resource> getAllResourceByVpIndex() throws IOException {
         return transformToResourceListLevelTwo(
                 transformToResourceListLevelOne(fetchVpIndex(1)),fetchVpIndex(2));

    }


    private JsonArray fetchVpIndex(int level) throws IOException {
        ClassPathResource queryfile;
        if (level == 1){
            queryfile = new ClassPathResource("static/query_level1.sparql");
        } else if (level == 2) {
            queryfile = new ClassPathResource("static/query_level2.sparql");
        } else {
            throw new IOException("Only level 1 and level supported so far. Please check input arguments!");
        }
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


    private List<Resource> transformToResourceListLevelOne (JsonArray resources) {
        List<Resource> resourceList = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++){
            JsonElement element = resources.get(i);
            Resource resource = new Resource();

            if (element.getAsJsonObject().getAsJsonObject("resourceTypeURI")!=null) {
                List<ResourceType> resourceTypes = new ArrayList<>();
                switch(element.getAsJsonObject().getAsJsonObject("resourceTypeURI").
                        get("value").getAsString()){
                    case "http://purl.org/ejp-rd/vocabulary/PatientRegistry":
                        resourceTypes.add(ResourceType.PATIENT_REGISTRY);
                        break;
                    case "http://purl.obolibrary.org/obo/OBIB_0000616",
                        "https://w3id.org/ejp-rd/vocabulary#Biobank":
                        resourceTypes.add(ResourceType.BIO_BANK);
                        break;
                    case "http://www.w3.org/ns/dcat#Dataset":
                        resourceTypes.add(ResourceType.DATASET);
                        break;
                    case "GUIDELINE":
                        resourceTypes.add(ResourceType.GUIDELINE);
                        break;
                    case "http://www.w3.org/ns/dcat#Catalog":
                        resourceTypes.add(ResourceType.CATALOG);
                        break;
                    default:
                        continue;
                }
                resource.setResourceType(resourceTypes);

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

                if (element.getAsJsonObject().getAsJsonObject("resourceCreated")!=null) {
                  String ts = element.getAsJsonObject().getAsJsonObject("resourceCreated").
                            get("value").getAsString();
                    Instant instant = Instant.parse(ts);
                    Date date = Date.from(instant);
                    Timestamp tsp = new Timestamp(date.getTime());
                    resource.setCreated(tsp);
                }else{
                    resource.setCreated(null);
                }

                if (element.getAsJsonObject().getAsJsonObject("resourceUpdated")!=null) {
                    String ts =element.getAsJsonObject().getAsJsonObject("resourceUpdated").
                            get("value").getAsString();
                    Instant instant = Instant.parse(ts);
                    Date date = Date.from(instant);
                    Timestamp tsp = new Timestamp(date.getTime());
                    resource.setUpdated(tsp);
                }else{
                    resource.setUpdated(null);
                }

                if (element.getAsJsonObject().getAsJsonObject("resource")!=null) {
                    resource.setResourceUri(element.getAsJsonObject().getAsJsonObject("resource").
                            get("value").getAsString());
                }else{
                    resource.setResourceUri("");
                }

                if (element.getAsJsonObject().getAsJsonObject("resourceLogo")!=null) {
                    resource.setLogo(element.getAsJsonObject().getAsJsonObject("resourceLogo").
                            get("value").getAsString());
                }else{
                    resource.setLogo("");
                }

                resourceList.add(resource);
            }

        }

        //filter resourceList by unique resourceName entries
        /*resourceList = resourceList
                .stream()
                .collect(Collectors.groupingBy(
                        Resource::getResourceUri,
                        Collectors.maxBy(Comparator.comparing(Resource::getId))
                ))
                .values()
                .stream()
                .map(opt -> opt.orElse(null))
                .collect(Collectors.toList());*/

        resourceList = resourceList.stream().filter(distinctByKey(Resource::getResourceUri)).toList();

        return resourceList;
    }

    private List<Resource> transformToResourceListLevelTwo (List<Resource> resourceList, JsonArray resources) {

        for (int i = 0; i < resources.size(); i++) {
            JsonElement element = resources.get(i);

            for (Resource resource : resourceList) {
                if (resource.getResourceUri().equals(
                        element.getAsJsonObject().getAsJsonObject("dataset").
                                get("value").getAsString())) {

                    if (element.getAsJsonObject().getAsJsonObject("operationType") != null) {
                        List<QueryType> queryTypeList = new ArrayList<>();
                        switch (element.getAsJsonObject().getAsJsonObject("operationType").
                                get("value").getAsString()) {
                            case "https://w3id.org/ejp-rd/vocabulary#VPBeacon2_individuals":
                                queryTypeList.add(QueryType.BEACON_INDIVIDUALS);
                                break;
                            case "https://w3id.org/ejp-rd/vocabulary#VPBeacon2_catalog":
                                queryTypeList.add(QueryType.BEACON_CATALOG);
                                break;
                            /*case "https://w3id.org/ejp-rd/vocabulary#VPBeacon2_biosamples":
                                queryTypeList.add(QueryType.BEACON_CATALOG)
                                break;*/
                            default:
                                continue;
                        }
                        resource.setQueryType(queryTypeList);

                        resource.setQueryable(true);

                        if (element.getAsJsonObject().getAsJsonObject("serviceURL")!=null) {
                            resource.setResourceAddress(element.getAsJsonObject().
                                    getAsJsonObject("serviceURL").get("value").getAsString());
                        }else{
                            resource.setQueryable(false);
                        }

                        //set default
                        resource.setEmail("abishaa.vengadeswaran@ejprd-project.eu");

                    } else {
                        resource.setQueryable(false);
                    }
                }
            }
        }

        return resourceList;
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
