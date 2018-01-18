package com.pixformance.themovie;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.search.DetailsFragment;
import com.pixformance.themovie.search.SearchListFragment;

/**
 * Main class responsible to manage the navigation and comunnication of the
 * screens (Search list and details).
 * In case of tablets boths screens are showed at the same time
 * In case of phones the screens are showed once a time
 */
public class MainActivity extends AppCompatActivity implements
        SearchListFragment.OnSelectMovie,
        FragmentManager.OnBackStackChangedListener{

    private static final String TAG_LIST = "list";
    private static final String TAG_DETAIL = "detail";
    private SearchListFragment mSearchListFragment;
    private DetailsFragment mDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(TAG_LIST) == null) {
            mSearchListFragment = new SearchListFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_search, mSearchListFragment, TAG_LIST)
                    .commit();
        } else {
            mSearchListFragment = (SearchListFragment) fragmentManager.findFragmentByTag(TAG_LIST);
        }
        mSearchListFragment.setOnSelectMovie(this);
        fragmentManager.addOnBackStackChangedListener(this);
        onBackStackChanged();


        if(findViewById(R.id.fragment_details) != null) { // if is tablet, shows the screen splitted
            if(fragmentManager.findFragmentByTag(TAG_DETAIL) == null) {
                mDetailsFragment = new DetailsFragment();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_details, mDetailsFragment, TAG_DETAIL)
                        .commit();
            } else {
                mDetailsFragment = (DetailsFragment) fragmentManager.findFragmentByTag(TAG_DETAIL);
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        ActionBar actionBar = getSupportActionBar();
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName());
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.app_name);
        }
    }

    @Override
    public void onSelectMovie(Movie movie) {
        if(mDetailsFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.slide_enter_from_right,
                            R.animator.slide_exit_to_left,
                            R.animator.slide_enter_from_left,
                            R.animator.slide_exit_to_right)
                    .replace(R.id.fragment_search, DetailsFragment.newInstance(movie), TAG_DETAIL)
                    .addToBackStack(movie.getTitle())
                    .commitAllowingStateLoss();
        } else {
            mDetailsFragment.showMovieDetails(movie);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}