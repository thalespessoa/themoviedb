package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.SearchResult;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class DataSource {

    private NetworkApi mNetworkApi;
    private LocalStore mLocalStore;
    private Call mCurrentCall;

    public interface OnFecthMovies {
        void onFetchSuccess(int page, SearchResult searchResult);
        void onError(HttpException httpException);
    }

    public interface OnFecthSuggestion {
        void onFetchSuccess(List<String> suggestions);
    }

    public DataSource(NetworkApi networkApi, LocalStore localStore) {
        mNetworkApi = networkApi;
        mLocalStore = localStore;
    }

    public void search(final String term, final int page, final OnFecthMovies onFecthMovies) {
        if(mCurrentCall != null) {
            mCurrentCall.cancel();
        }
        mCurrentCall = mNetworkApi.search(term, page);
        mCurrentCall.enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                mCurrentCall = null;
                if(response.getTotalResults() == 0) {
                    onFecthMovies.onError(new HttpException(HttpException.ERROR_EMPTY));
                } else {
                    mLocalStore.save(term);
                    onFecthMovies.onFetchSuccess(page, response);
                }
            }

            @Override
            public void onError(HttpException exception) {
                mCurrentCall = null;
                onFecthMovies.onError(exception);
            }
        });
    }

    public void searchSuggestion(String term, final OnFecthSuggestion onFecthSuggestion) {
        mLocalStore.search(term, onFecthSuggestion);
    }

    public void deleteSuggestion(String term, final OnFecthSuggestion onFecthSuggestion) {
        mLocalStore.search(term, onFecthSuggestion);
    }
}
