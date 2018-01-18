package com.pixformance.themovie.search;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.pixformance.themovie.app.ApplicationController;
import com.pixformance.themovie.R;
import com.pixformance.themovie.data.DataProvider;
import com.pixformance.themovie.data.HttpException;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;
import com.pixformance.themovie.util.TextUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Search screen
 *
 * Created by thalespessoa on 1/16/18.
 */

public class SearchListFragment extends Fragment implements
        SearchView.OnQueryTextListener,
        MoviesAdapter.MoviesAdapterCallback,
        SuggestionAdapter.SuggestionAdapterCallback,
        DataProvider.OnFecthMovies,
        DataProvider.OnFecthSuggestion,
        View.OnFocusChangeListener {

    @Inject
    protected DataProvider mDataProvider;

    @BindView(R.id.progress)
    protected ProgressBar mProgress;
    @BindView(R.id.search_view)
    protected SearchView mSearchView;
    @BindView(R.id.rv_movies)
    protected RecyclerView mMoviesRecyclerView;
    @BindView(R.id.rv_suggestions)
    protected RecyclerView mSuggestionRecyclerView;
    @BindView(R.id.overlay)
    protected View mOverlay;

    private OnSelectMovie onSelectMovie;
    private MoviesAdapter mMoviesAdapter;
    private SuggestionAdapter mSuggestionAdapter;
    private String mCurrentQuery;
    private int mCurrentPage;
    private int mLastPageRequested;

    /**
     * Callback to handle movie selection
     * @param onSelectMovie
     */
    public void setOnSelectMovie(OnSelectMovie onSelectMovie) {
        this.onSelectMovie = onSelectMovie;
    }

    public interface OnSelectMovie {
        void onSelectMovie(Movie movie);
    }

    // ---------------------------------------------------------------------------------------------
    // Lifecycle
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((ApplicationController)getActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        mMoviesAdapter = new MoviesAdapter();
        mSuggestionAdapter = new SuggestionAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        mSearchView.setOnQueryTextFocusChangeListener(this);
        mSearchView.setOnQueryTextListener(this);

        mMoviesAdapter.setMoviesAdapterCallback(this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMoviesRecyclerView.setHasFixedSize(true);

        mSuggestionAdapter.setSuggestionAdapterCallback(this);
        mSuggestionRecyclerView.setAdapter(mSuggestionAdapter);
        mSuggestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMoviesRecyclerView.setHasFixedSize(true);

        mDataProvider.setOnFecthMovies(this);
        mDataProvider.setOnFecthSuggestions(this);

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataProvider.setOnFecthMovies(null);
        mDataProvider.setOnFecthSuggestions(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(mMoviesAdapter.getMovies().size() == 0) {
            mSearchView.requestFocus();
        } else {
            mMoviesRecyclerView.requestFocus();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Interactions
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onSelectSuggestion(String suggestion) {
        mSearchView.setQuery(suggestion, true);
        mMoviesRecyclerView.requestFocus();
    }

    @Override
    public void onSelectMovie(Movie movie) {
        onSelectMovie.onSelectMovie(movie);
    }

    @Override
    public void onScrolledDown() {
        if (mLastPageRequested == mCurrentPage) {
            mDataProvider.searchMovie(mCurrentQuery, ++mLastPageRequested);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurrentQuery = query;
        mCurrentPage = 1;
        mLastPageRequested = 1;
        mMoviesAdapter.clear();
        mProgress.setVisibility(View.VISIBLE);
        mDataProvider.searchMovie(mCurrentQuery, mCurrentPage);
        mMoviesRecyclerView.requestFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataProvider.searchSuggestion(newText);
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mSearchView) {
            if (hasFocus) {
                mDataProvider.searchSuggestion(mSearchView.getQuery().toString());
                mSuggestionRecyclerView.setVisibility(View.VISIBLE);
                mOverlay.setVisibility(View.VISIBLE);
            } else {
                mSuggestionRecyclerView.setVisibility(View.GONE);
                mOverlay.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.overlay)
    public void onClickOverlay() {
        mMoviesRecyclerView.requestFocus();
    }

    // ---------------------------------------------------------------------------------------------
    // Callbacks from DataProvider
    // ---------------------------------------------------------------------------------------------

    // Movies

    @Override
    public void onFetchMoviesSuccess(int page, SearchResult searchResult) {
        mProgress.setVisibility(View.INVISIBLE);
        mCurrentPage = searchResult.getPage();
        mMoviesAdapter.addMovies(searchResult.getResults(),
                searchResult.getPage() == searchResult.getTotalPages());
    }

    @Override
    public void onError(HttpException httpException) {
        mProgress.setVisibility(View.INVISIBLE);

        String message = httpException.getMessage();
        if(message == null) {
            message = TextUtil.getErrorMessage(getActivity(), httpException.getCode());
        }

        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setNegativeButton("OK", null)
                .show();
    }

    // Suggestions

    @Override
    public void onFetchSuggestionsSuccess(List<String> suggestions) {
        mSuggestionAdapter.setData(suggestions);
    }
}