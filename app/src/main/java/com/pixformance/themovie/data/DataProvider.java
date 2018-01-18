package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.SearchResult;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class DataProvider {

    private NetworkApi.SearchApi mSearchApi;
    private LocalStore mLocalStore;
    private Call mCurrentCall;
    private OnFecthMovies onFetchMovies;
    private OnFecthSuggestion onFetchSuggestions;

    public void setOnFecthMovies(OnFecthMovies onFecthMovies) {
        this.onFetchMovies = onFecthMovies;
    }

    public void setOnFecthSuggestions(OnFecthSuggestion onFecthSuggestions) {
        this.onFetchSuggestions = onFecthSuggestions;
    }

    public interface OnFecthMovies {
        void onFetchMoviesSuccess(int page, SearchResult searchResult);
        void onError(HttpException httpException);
    }

    public interface OnFecthSuggestion {
        void onFetchSuggestionsSuccess(List<String> suggestions);
    }

    public DataProvider(NetworkApi.SearchApi networkApi, LocalStore localStore) {
        mSearchApi = networkApi;
        mLocalStore = localStore;
    }

    public void searchMovie(final String term, final int page) {
        if(mCurrentCall != null) {
            mCurrentCall.cancel();
        }
        mCurrentCall = mSearchApi.search(term, page);
        mCurrentCall.enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                mCurrentCall = null;
                if(response.getTotalResults() == 0) {
                    if(onFetchMovies != null) {
                        onFetchMovies.onError(new HttpException(HttpException.ERROR_EMPTY));
                    }
                } else {
                    mLocalStore.save(term);
                    if(onFetchMovies != null) {
                        onFetchMovies.onFetchMoviesSuccess(page, response);
                    }
                }
            }

            @Override
            public void onError(HttpException exception) {
                mCurrentCall = null;

                if(onFetchMovies != null) {
                    onFetchMovies.onError(exception);
                }
            }
        });
    }

    public void searchSuggestion(String term) {
        mLocalStore.search(term, onFetchSuggestions);
    }
}
