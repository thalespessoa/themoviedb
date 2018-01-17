package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;

import java.util.List;

import okhttp3.Headers;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class DataSource {

    private NetworkApi mNetworkApi;
    private LocalStore mLocalStore;


    public interface OnFecthMovies {
        void onFetchSuccess(int page, SearchResult searchResult);
        void onError(String httpException);
    }

    public interface OnFecthSuggestion {
        void onFetchSuccess(List<String> suggestions);
    }

    public DataSource(NetworkApi networkApi, LocalStore localStore) {
        mNetworkApi = networkApi;
        mLocalStore = localStore;
    }

    public void search(final String term, final int page, final OnFecthMovies onFecthMovies) {
        mNetworkApi.search(term, page).enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                mLocalStore.save(term);
                onFecthMovies.onFetchSuccess(page, response);
            }

            @Override
            public void onError(String exception) {
                onFecthMovies.onError(exception);
            }
        });
    }

    public void searchSuggestion(String term, final OnFecthSuggestion onFecthSuggestion) {
        mLocalStore.search(term, onFecthSuggestion);
    }
}
