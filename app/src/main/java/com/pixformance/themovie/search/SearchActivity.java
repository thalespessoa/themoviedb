package com.pixformance.themovie.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.model.Movie;

public class SearchActivity extends AppCompatActivity implements SearchListFragment.OnSelectMovie{

    SearchListFragment mSearchListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchListFragment = (SearchListFragment) getFragmentManager().findFragmentById(R.id.fragment_search);
        mSearchListFragment.setOnSelectMovie(new SearchListFragment.OnSelectMovie() {
            @Override
            public void onSelectMovie(Movie movie) {
                System.out.println("SearchActivity.onSelectMovie: "+movie.getTitle());
            }
        });
    }

    @Override
    public void onSelectMovie(Movie movie) {

    }
}