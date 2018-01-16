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
import com.pixformance.themovie.data.NetworkApi;
import com.pixformance.themovie.data.ServiceCallback;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.data.model.SearchResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import okhttp3.Headers;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class SearchListFragment extends Fragment implements SearchListContract.View, SearchView.OnQueryTextListener {

    OnSelectMovie onSelectMovie;

    @Inject
    NetworkApi mNetworkApi;

    SearchListContract.Presenter mPresenter;

    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    MoviesAdapter mMoviesAdapter;

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
        mMoviesAdapter.setOnSelectMovie(new MoviesAdapter.OnSelectMovie() {
            @Override
            public void onSelectMovie(Movie movie) {
                onSelectMovie.onSelectMovie(movie);
            }
        });
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("SearchListFragment.onQueryTextSubmit: "+query);
        mNetworkApi.search(query, 1).enqueue(new ServiceCallback<SearchResult>() {
            @Override
            public void onSuccess(Headers headers, SearchResult response) {
                mMoviesAdapter.setMovies(response.getResults());
            }

            @Override
            public void onError(String exception) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("SearchListFragment.onQueryTextChange: "+newText);
        return false;
    }

}
