package com.pixformance.themovie.search;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
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
    @BindView(R.id.iv_poster)
    ImageView mPosterImageVIew;

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
        mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        mTitleTextView.setText(mMovie.getTitle());
        mDescriptionTextView.setText(mMovie.getOverview());
        mVoteTextView.setText(String.valueOf(mMovie.getVoteAverage()));

        Picasso.with(getActivity())
                .load(String.format("https://image.tmdb.org/t/p/w92/%s", mMovie.getPosterPath()))
                .fit()
                .into(mPosterImageVIew);

        return view;
    }
}
