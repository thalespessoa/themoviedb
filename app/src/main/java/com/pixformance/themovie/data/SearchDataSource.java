package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.SearchResult;

import okhttp3.Headers;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class SearchDataSource {

    NetworkApi mNetworkApi;


    public interface SearchDataSourceCallback<T> {
        void onFetchSuccess(int page, T object);
        void onError(String httpException);
    }

    public SearchDataSource(NetworkApi networkApi) {
        mNetworkApi = networkApi;
    }

    public void search(String term, final int page, final SearchDataSourceCallback<SearchResult> serviceCallback) {
        mNetworkApi.search(term, page).enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                serviceCallback.onFetchSuccess(page, response);
            }

            @Override
            public void onError(String exception) {
                serviceCallback.onError(exception);
            }
        });
    }
}
