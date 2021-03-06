package com.pixformance.themovie.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by thalespessoa on 1/16/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable {

    String title;
    String overview;
    @JsonProperty("original_title")
    String originalTitle;
    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("vote_average")
    float voteAverage;
    @JsonProperty("vote_count")
    int voteCount;
    @JsonProperty("backdrop_path")
    String backdropPath;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
