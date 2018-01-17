package com.pixformance.themovie.data;

import com.pixformance.themovie.data.local.SuggestionsPersistence;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;
import com.pixformance.themovie.data.remote.NetworkApi;
import com.pixformance.themovie.data.remote.ServiceCallback;

import java.util.List;

import okhttp3.Headers;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class DataSource {

    NetworkApi mNetworkApi;
    SuggestionsPersistence mSuggestionsPersistence;


    public interface OnFecthMovies {
        void onFetchSuccess(int page, List<Movie> movies);
        void onError(String httpException);
    }

    public interface OnFecthSuggestion {
        void onFetchSuccess(List<String> suggestions);
    }

    public DataSource(NetworkApi networkApi, SuggestionsPersistence suggestionsPersistence) {
        mNetworkApi = networkApi;
        mSuggestionsPersistence = suggestionsPersistence;
    }

    public void search(final String term, final int page, final OnFecthMovies onFecthMovies) {
        mNetworkApi.search(term, page).enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                mSuggestionsPersistence.save(term);
                onFecthMovies.onFetchSuccess(page, response.getResults());
            }

            @Override
            public void onError(String exception) {
                onFecthMovies.onError(exception);
            }
        });
    }

    public void searchSuggestion(String term, final OnFecthSuggestion onFecthSuggestion) {
        mSuggestionsPersistence.search(term, onFecthSuggestion);
    }
}
