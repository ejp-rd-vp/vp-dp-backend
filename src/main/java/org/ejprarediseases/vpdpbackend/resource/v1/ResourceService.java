package org.ejprarediseases.vpdpbackend.resource.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.ejprarediseases.vpdpbackend.utils.StringConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResourceService {

    @Value("${application.resourcesAuthKeys}")
    private String resourcesAuthKeys;

    /**
     * Retrieves all resources from the "resources.txt" file.
     *
     * @return A list of Resource objects read from the file.
     * @throws IOException if there's an error reading the resources file.
     */
    public List<Resource> getAllResources() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(readResourcesFileAsInputStream(), new TypeReference<List<Resource>>() {});
    }

    public List<Resource> getAllQueryableResources() throws IOException {
        return getAllResources().stream().filter(Resource::isQueryable).toList();
    }

    /**
     * Reads the "resources.txt" file from the classpath and returns it as an InputStream.
     *
     * @return An InputStream representing the "resources.txt" file.
     * @throws IOException if there's an error reading the file.
     */
    private InputStream readResourcesFileAsInputStream() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/resources.txt");
        return resource.getInputStream();
    }

    /**
     * Retrieves a list of all resource IDs available.
     *
     * @return A list of strings representing the resource IDs.
     * @throws IOException if there's an error reading the resources file.
     */
    public List<String> getAllResourceIds() throws IOException {
        return getAllResources().stream().map(Resource::getId).toList();
    }

    /**
     * Retrieves a specific resource by its ID.
     *
     * @param resourceId The ID of the resource to retrieve.
     * @return The Resource object with the specified ID.
     * @throws NoSuchElementException if the resource with the given ID is not found.
     * @throws IOException if there's an error reading the resources file.
     */
    public Resource getResourceById(String resourceId) throws NoSuchElementException, IOException {
        Optional<Resource> optionalResource =
                getAllResources().stream().filter(re -> re.getId().equals(resourceId)).findFirst();
        if (optionalResource.isPresent()) {
            return optionalResource.get();
        } else {
            throw new NoSuchElementException("Resource Id: " + resourceId + "does not exist.");
        }
    }


    /**
     * Retrieves the authorization key associated with a specific resource ID.
     *
     * This method fetches the authorization key from a map of resource IDs to their respective
     * authorization keys. The provided resource ID is used to look up the corresponding
     * authorization key.
     *
     * @param resourceId The unique identifier of the resource for which the authorization key is to be retrieved.
     * @return The authorization key associated with the specified resource ID, or null if no matching key is found.
     */
    public String getResourceAuthKeyById(String resourceId) {
        Map<String, String> authKeys = StringConverter.toHashMap(resourcesAuthKeys);
        return authKeys.get(resourceId);
    }
}
