package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by thalespessoa on 1/16/18.
 */

public interface NetworkApi {
    @GET("/3/search/movie")
    Call<SearchResult> search(@Query("query") String query, @Query("page") int page);
}
