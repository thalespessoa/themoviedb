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
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.util.TextUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class DetailsFragment extends Fragment {

    static private String ARG_MOVIE = "ARG_MOVIE";

    Movie mMovie;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;
    @BindView(R.id.tv_vote)
    TextView mVoteTextView;
    @BindView(R.id.tv_vote_count)
    TextView mVoteCountTextView;
    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

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

    public void showMovieDetails(Movie movie) {
        this.mMovie = movie;

        mDescriptionTextView.setText(mMovie.getOverview());
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
            Picasso.with(getActivity())
                    .load(String.format("https://image.tmdb.org/t/p/w92/%s", mMovie.getPosterPath()))
                    .fit()
                    .into(mPosterImageView);
        }
    }
}
