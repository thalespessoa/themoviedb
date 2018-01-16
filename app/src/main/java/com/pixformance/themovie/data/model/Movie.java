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
    @JsonProperty("poster_path")
    String posterPath;
    @JsonProperty("vote_average")
    float voteAverage;
    @JsonProperty("vote_count")
    float voteCount;

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

    public float getVoteCount() {
        return voteCount;
    }
}
