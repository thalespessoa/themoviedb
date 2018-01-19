package com.pixformance.themovie.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.NetworkApi;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.util.ImageUtil;
import com.pixformance.themovie.util.TextUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Details screen
 *
 * Created by thalespessoa on 1/16/18.
 */

public class DetailsFragment extends Fragment {

    static private String ARG_MOVIE = "ARG_MOVIE";

    Movie mMovie;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;
    @BindView(R.id.tv_vote)
    TextView mVoteTextView;
    @BindView(R.id.tv_vote_count)
    TextView mVoteCountTextView;
    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;
    @BindView(R.id.iv_landscape)
    ImageView mLandscapeImageView;

    public static DetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        if(mMovie != null) {
            showMovieDetails(mMovie);
        }

        setRetainInstance(true);

        return view;
    }

    /**
     * Shows movie information on the screen
     * @param movie
     */
    public void showMovieDetails(Movie movie) {
        this.mMovie = movie;

        mDescriptionTextView.setText(mMovie.getOverview());
        mTitleTextView.setText(mMovie.getTitle());
        mVoteTextView.setText(TextUtil.formatAvarageVote(movie));
        if(movie.getVoteCount() == 0) {
            mVoteCountTextView.setText(R.string.no_votes);
        } else {
            mVoteCountTextView.setText(String.format(getString(R.string.vote_count),
                    String.valueOf(movie.getVoteCount())));
        }

        if(mMovie.getPosterPath() == null || mMovie.getPosterPath().equals("")) {
            mPosterImageView.setVisibility(View.GONE);
        } else {
            mPosterImageView.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(mPosterImageView, mMovie.getPosterPath());
        }


        if(mMovie.getBackdropPath() == null || mMovie.getBackdropPath().equals("")) {
            mLandscapeImageView.setVisibility(View.GONE);
        } else {
            mLandscapeImageView.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(mLandscapeImageView, mMovie.getBackdropPath());
        }
    }
}
