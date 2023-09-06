package org.ejprarediseases.vpdpbackend.resource.v1;

import lombok.SneakyThrows;
import org.ejprarediseases.vpdpbackend.resource.v1.model.Resource;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@Tag("UnitTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {ResourceService.class})
@DisplayName("Resource Service Unit Tests")
public class ResourceServiceTest {

    ResourceService resourceService;

    @BeforeEach
    void setUp() {
        resourceService = new ResourceService();
    }

    @Test
    @Order(1)
    @SneakyThrows
    public void shouldGetAllResources() {
        List<Resource> resources = resourceService.getAllResources();
        assert(resources.size() > 1);
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void shouldGetAllQueryableResources() {
        List<Resource> allResources = resourceService.getAllResources();
        List<Resource> queryableResources = resourceService.getAllQueryableResources();
        assert (allResources.size() >= queryableResources.size());
        for(Resource resource : queryableResources) {
            assert (resource.isQueryable());
        }
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void shouldGetResourceById() {
        List<Resource> allResources = resourceService.getAllResources();
        Resource firstResource = allResources.get(0);
        Resource retrievedResource = resourceService.getResourceById(firstResource.getId());
        assert (firstResource.equals(retrievedResource));
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void shouldThrowExceptionWhenResourceIdDoesNotExist() {
        assertThrows(NoSuchElementException.class, () -> resourceService.getResourceById("NOT_AN_ID"));
    }

}
