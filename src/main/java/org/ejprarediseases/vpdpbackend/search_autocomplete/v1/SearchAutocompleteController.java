package org.ejprarediseases.vpdpbackend.search_autocomplete.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/autocomplete")
public class SearchAutocompleteController {

    final SearchAutocompleteService searchAutocompleteService;

    /**
     * Executes a search for autocomplete results based on the given query and pagination parameters.
     *
     * @param query The search query.
     * @param page The page number for pagination.
     * @param size The number of results per page.
     * @return The ResponseEntity containing the search autocomplete results.
     */
    @GetMapping()
    public ResponseEntity executeSearch(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam int size) {
        SearchAutocompleteResult searchResult = searchAutocompleteService.getSearchResults(query, page, size);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
}
