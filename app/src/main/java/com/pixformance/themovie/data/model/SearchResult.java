package com.pixformance.themovie.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by thalespessoa on 1/16/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    int page;
    @JsonProperty("total_results")
    int totalResults;
    @JsonProperty("total_pages")
    int totalPages;
    List<Movie> results;

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }
}
