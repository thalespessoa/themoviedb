package com.pixformance.themovie.data;

import com.pixformance.themovie.data.model.SearchResult;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;

/**
 * Class responsible for manage all data provided to the application.
 * This class should be inject by Dagger in any place of the
 * application that needs data access (remotely or locally)
 *
 * Created by thalespessoa on 1/16/18.
 */

public class DataProvider {

    private NetworkApi.SearchApi mSearchApi;
    private LocalStore mLocalStore;
    private Call mCurrentCall;
    private OnFecthMovies onFetchMovies;
    private OnFecthSuggestion onFetchSuggestions;

    /**
     * Set listener to movies search
     * @param onFecthMovies
     */
    public void setOnFecthMovies(OnFecthMovies onFecthMovies) {
        this.onFetchMovies = onFecthMovies;
    }

    /**
     * Set listener to suggestions
     * @param onFecthSuggestions
     */
    public void setOnFecthSuggestions(OnFecthSuggestion onFecthSuggestions) {
        this.onFetchSuggestions = onFecthSuggestions;
    }

    // Callback interfaces

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


    /**
     * Perform movie search remotely.
     * Before call this method you need use 'setOnFecthMovies' method to listener results
     *
     * @param term word to be searched
     */
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

    /**
     * Perform suggestion search locally
     * Before call this method you need use 'setOnFecthSuggestions' method to listener results
     *
     * @param term word to be searched
     */
    public void searchSuggestion(String term) {
        mLocalStore.search(term, onFetchSuggestions);
    }
}
