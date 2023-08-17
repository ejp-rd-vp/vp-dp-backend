package org.ejprarediseases.vpdpbackend.mapping.v1;

import jakarta.transaction.Transactional;
import org.ejprarediseases.vpdpbackend.utils.StringConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class MappingService {

    @Value("${application.geneMappingUrl}")
    private String geneMappingUrl;

    /**
     * Retrieves gene mapping information for the specified HGNC ID.
     *
     * @param hgncId The HGNC (HUGO Gene Nomenclature Committee) ID of the gene to fetch mapping for.
     * @return A GeneMapping object containing the HGNC ID and a list of Orphanet codes associated with the gene.
     */
    @Transactional
    public GeneMapping getGeneMapping(String hgncId) {
        String fetchedMappingResponse = fetchGeneMapping(hgncId);
        List<String> orphaCodes =
                StringConverter.getAllSubstringsMatchingRegex(fetchedMappingResponse, "Orphanet_(\\d*)");
        return new GeneMapping(hgncId, orphaCodes);
    }

    /**
     * Fetches gene mapping information from the external service based on the provided HGNC ID.
     *
     * @param hgncId The HGNC (HUGO Gene Nomenclature Committee) ID of the gene to fetch mapping for.
     * @return The raw JSON response as a String from the external service.
     */
    private String fetchGeneMapping(String hgncId) {
        WebClient client = WebClient.create();
        return client.get()
                .uri( geneMappingUrl + "?by=hgnc&input=" + hgncId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
