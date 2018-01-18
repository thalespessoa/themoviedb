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

import com.pixformance.themovie.ApplicationController;
import com.pixformance.themovie.R;
import com.pixformance.themovie.data.DataSource;
import com.pixformance.themovie.data.HttpException;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;
import com.pixformance.themovie.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class SearchListFragment extends Fragment implements
        SearchView.OnQueryTextListener,
        MoviesAdapter.MoviesAdapterCallback,
        SuggestionAdapter.SuggestionAdapterCallback,
        DataSource.OnFecthMovies,
        DataSource.OnFecthSuggestion,
        View.OnFocusChangeListener {

    @Inject
    protected DataSource mDataSource;

    @BindView(R.id.progress)
    protected ProgressBar mProgress;
    @BindView(R.id.search_view)
    protected SearchView mSearchView;
    @BindView(R.id.rv_movies)
    protected RecyclerView mMoviesRecyclerView;
    @BindView(R.id.rv_suggestions)
    protected RecyclerView mSuggestionRecyclerView;

    private OnSelectMovie onSelectMovie;
    private MoviesAdapter mMoviesAdapter;
    private SuggestionAdapter mSuggestionAdapter;
    private String mCurrentQuery;
    private int mCurrentPage;
    private int mLastPageRequested;
    private View mRootView;

    public void setOnSelectMovie(OnSelectMovie onSelectMovie) {
        this.onSelectMovie = onSelectMovie;
    }

    public interface OnSelectMovie {
        void onSelectMovie(Movie movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((ApplicationController)getActivity().getApplication()).getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);

        mMoviesAdapter = new MoviesAdapter();
        mSuggestionAdapter = new SuggestionAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        mSearchView.setOnQueryTextFocusChangeListener(this);

        mMoviesAdapter.setMoviesAdapterCallback(this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMoviesRecyclerView.setHasFixedSize(true);

        mSuggestionAdapter.setSuggestionAdapterCallback(this);
        mSuggestionRecyclerView.setAdapter(mSuggestionAdapter);
        mSuggestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMoviesRecyclerView.setHasFixedSize(true);

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSearchView.setOnQueryTextListener(this);
        mSuggestionAdapter.setData(new ArrayList<String>());
    }

    @Override
    public void onStop() {
        super.onStop();
        mSearchView.setOnQueryTextListener(null);
        mSuggestionRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mSearchView) {
            if (hasFocus) {
                mDataSource.searchSuggestion(mSearchView.getQuery().toString(), SearchListFragment.this);
                mSuggestionRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mSuggestionRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    // SearchView

    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurrentQuery = query;
        mCurrentPage = 1;
        mLastPageRequested = 1;
        mMoviesAdapter.clear();
        mProgress.setVisibility(View.VISIBLE);
        mDataSource.search(mCurrentQuery, mCurrentPage, this);
        mMoviesRecyclerView.requestFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mDataSource.searchSuggestion(newText, this);
        return false;
    }

    // Suggestion

    @Override
    public void onSelectSuggestion(String suggestion) {
        mSearchView.setQuery(suggestion, true);
        mMoviesRecyclerView.requestFocus();
    }

    @Override
    public void onFetchSuccess(List<String> suggestions) {
        mSuggestionAdapter.setData(suggestions);
    }

    // Scroll

    @Override
    public void onSelectMovie(Movie movie) {
        onSelectMovie.onSelectMovie(movie);
    }

    @Override
    public void onScrolledDown() {
        if (mLastPageRequested == mCurrentPage) {
            mDataSource.search(mCurrentQuery, ++mLastPageRequested, this);
        }
    }

    // Data Source


    @Override
    public void onFetchSuccess(int page, SearchResult searchResult) {
        mProgress.setVisibility(View.INVISIBLE);
        mCurrentPage = searchResult.getPage();
        mMoviesAdapter.addMovies(searchResult.getResults(), searchResult.getPage() == searchResult.getTotalPages());
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
}