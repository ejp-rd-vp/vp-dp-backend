package org.ejprarediseases.vpdpbackend.hierarchy.v1;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

@WebFluxTest
@ActiveProfiles("test")
@Tag("UnitTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {HierarchyService.class})
@DisplayName("Hierarchy Service Unit Tests")
public class HierarchyServiceTest {

    @Value("${application.hierarchyUrl}")
    private String hierarchyUrl;

    private HierarchyService hierarchyService;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        hierarchyService = new HierarchyService();
    }


    @Test
    public void getWebClientMock() {
        final var mock = Mockito.mock(WebClient.class);
        final var uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        final var responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        when(mock.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(ArgumentMatchers.<String>notNull())).thenReturn(headersSpecMock);
        when(headersSpecMock.header(notNull(), notNull())).thenReturn(headersSpecMock);
        when(headersSpecMock.headers(notNull())).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
//        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<String>>notNull()))
//                .thenReturn(Mono.just(resp));

    }

//    @Test
//    @DisplayName("Test getOrphaCodeHierarchyUp returns valid hierarchy")
//    void testGetOrphaCodeHierarchyUp() throws WebClientException {
//        // Mock WebClient behavior for UP direction
//        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
//        when(webClient.get().uri(anyString())).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
//        when(webClient.uri(anyString())).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.accept(any(MediaType.class))).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
//        when(webClient.bodyToMono(String.class)).thenReturn(mock(WebClient.MonoBodySpec.class));
//        when(webClient.block()).thenReturn("{ \"apiVersion\": \"1.0\", \"parents\": [...] }");
//
//        String orphaCode = "ORPHA123";
//        int numberOfLevels = 3;
//        String response = hierarchyService.getOrphaCodeHierarchyUp(orphaCode, numberOfLevels);
//
//        // Add assertions for the response content
//        // Verify that the WebClient was called with the correct URL and parameters
//        // Verify that the Hierarchy object is properly populated and returned
//    }

//    @Test
//    @DisplayName("Test getOrphaCodeHierarchyDown returns valid hierarchy")
//    void testGetOrphaCodeHierarchyDown() throws WebClientException {
//        // Mock WebClient behavior for DOWN direction
//        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
//        when(webClient.uri(anyString())).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.accept(any(MediaType.class))).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
//        when(webClient.bodyToMono(String.class)).thenReturn(mock(WebClient.MonoBodySpec.class));
//        when(webClient.block()).thenReturn("{ \"apiVersion\": \"1.0\", \"childs\": [...] }");
//
//        String orphaCode = "ORPHA456";
//        int numberOfLevels = 2;
//        String response = hierarchyService.getOrphaCodeHierarchyDown(orphaCode, numberOfLevels);
//
//        // Add assertions for the response content
//        // Verify that the WebClient was called with the correct URL and parameters
//        // Verify that the Hierarchy object is properly populated and returned
//    }
//
//    @Test
//    @DisplayName("Test convertOrphaCodeHierarchyUpToListOfOrphaCodeDto returns valid list")
//    void testConvertOrphaCodeHierarchyUpToListOfOrphaCodeDto() {
//        // Create test data
//        Hierarchy hierarchy = new Hierarchy();
//        // Add test data to hierarchy
//
//        List<OrphaCodeHierarchyDto> expectedResult = new ArrayList<>();
//        // Add expected result data
//
//        List<OrphaCodeHierarchyDto> result = hierarchyService.convertOrphaCodeHierarchyUpToListOfOrphaCodeDto(hierarchy);
//
//        // Add assertions to compare result and expectedResult
//    }
//
//    @Test
//    @DisplayName("Test convertOrphaCodeHierarchyDownToListOfOrphaCodeDto returns valid list")
//    void testConvertOrphaCodeHierarchyDownToListOfOrphaCodeDto() {
//        // Create test data
//        Hierarchy hierarchy = new Hierarchy();
//        // Add test data to hierarchy
//
//        List<OrphaCodeHierarchyDto> expectedResult = new ArrayList<>();
//        // Add expected result data
//
//        List<OrphaCodeHierarchyDto> result = hierarchyService.convertOrphaCodeHierarchyDownToListOfOrphaCodeDto(hierarchy);
//
//        // Add assertions to compare result and expectedResult
//    }
//
//    @Test
//    @DisplayName("Test getOrphaCodeHierarchy returns valid hierarchy combining UP and DOWN directions")
//    void testGetOrphaCodeHierarchy() throws JsonProcessingException {
//        // Create test data
//        String orphaCode = "ORPHA789";
//        int numberOfLevels = 1;
//        List<HierarchyWay> ways = new ArrayList<>();
//        ways.add(HierarchyWay.UP);
//        ways.add(HierarchyWay.DOWN);
//
//        // Mock WebClient behavior for UP direction
//        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
//        when(webClient.uri(anyString())).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.accept(any(MediaType.class))).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
//        when(webClient.bodyToMono(String.class)).thenReturn(mock(WebClient.MonoBodySpec.class));
//        when(webClient.block()).thenReturn("{ \"apiVersion\": \"1.0\", \"parents\": [...] }");
//
//        // Mock WebClient behavior for DOWN direction
//        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
//        when(webClient.uri(anyString())).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.accept(any(MediaType.class))).thenReturn(mock(WebClient.RequestHeadersSpec.class));
//        when(webClient.retrieve()).thenReturn(mock(WebClient.ResponseSpec.class));
//        when(webClient.bodyToMono(String.class)).thenReturn(mock(WebClient.MonoBodySpec.class));
//        when(webClient.block()).thenReturn("{ \"apiVersion\": \"1.0\", \"childs\": [...] }");
//
//        Hierarchy result = hierarchyService.getOrphaCodeHierarchy(orphaCode, ways, numberOfLevels);
//
//        // Add assertions for the Hierarchy object returned in the result
//        // Verify that both UP and DOWN WebClient calls were made with the correct URLs and parameters
//        // Verify that the Hierarchy object is properly populated and returned
//    }



}
