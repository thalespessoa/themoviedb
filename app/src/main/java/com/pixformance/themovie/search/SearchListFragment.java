package com.pixformance.themovie.search;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.SearchDataSource;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class SearchListFragment extends Fragment implements
        SearchView.OnQueryTextListener,
        MoviesAdapter.MoviesAdapterCallback,
        SearchDataSource.SearchDataSourceCallback<SearchResult> {

    OnSelectMovie onSelectMovie;

    @Inject
    SearchDataSource mSearchDataSource;

    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    MoviesAdapter mMoviesAdapter;
    String mCurrentQuery;
    int mCurrentPage;
    int mLastPageRequested;

    public void setOnSelectMovie(OnSelectMovie onSelectMovie) {
        this.onSelectMovie = onSelectMovie;
    }

    interface OnSelectMovie {
        void onSelectMovie(Movie movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        mSearchView.setOnQueryTextListener(this);

        mMoviesAdapter = new MoviesAdapter();
        mMoviesAdapter.setMoviesAdapterCallback(this);
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    // SearchView

    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurrentQuery = query;
        mCurrentPage = 1;
        mLastPageRequested = 1;
        mMoviesAdapter.setMovies(new ArrayList<Movie>());
        mSearchDataSource.search(mCurrentQuery, mCurrentPage, this);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Scroll

    @Override
    public void onSelectMovie(Movie movie) {
        onSelectMovie.onSelectMovie(movie);
    }

    @Override
    public void onScrolledDown() {
        if(mLastPageRequested == mCurrentPage) {
            mSearchDataSource.search(mCurrentQuery, ++mLastPageRequested, this);
        }
    }

    // Data Source

    @Override
    public void onFetchSuccess(int page, SearchResult searchResult) {
        System.out.println("SearchListFragment.onFetchSuccess");
        mCurrentPage = mLastPageRequested;
        mMoviesAdapter.addMovies(searchResult.getResults());
    }

    @Override
    public void onError(String httpException) {

    }

}
