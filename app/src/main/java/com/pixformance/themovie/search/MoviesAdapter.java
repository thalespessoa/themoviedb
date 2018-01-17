package com.pixformance.themovie.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter {

    static private int TYPE_MOVIE = 1;
    static private int TYPE_LOADING = 2;

    private List<Movie> mMovies = new ArrayList<>();
    private int mSize = 0;
    private boolean mLastPage;

    private MoviesAdapterCallback moviesAdapterCallback;

    public void setMoviesAdapterCallback(MoviesAdapterCallback moviesAdapterCallback) {
        this.moviesAdapterCallback = moviesAdapterCallback;
    }

    interface MoviesAdapterCallback {
        void onSelectMovie(Movie movie);
        void onScrolledDown();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_MOVIE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == TYPE_MOVIE) {
            final Movie movie = mMovies.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            movieViewHolder.title.setText(movie.getTitle());
            movieViewHolder.vote.setText(String.valueOf(movie.getVoteAverage()));
            Picasso.with(movieViewHolder.poster.getContext())
                    .load(String.format("https://image.tmdb.org/t/p/w92/%s",movie.getPosterPath()))
                    .fit()
                    .into(movieViewHolder.poster);

            movieViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moviesAdapterCallback.onSelectMovie(movie);
                }
            });
        } else {
            moviesAdapterCallback.onScrolledDown();
        }
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    @Override
    public int getItemViewType(int position) {
        if(mLastPage || (position < getItemCount() - 1)) {
            return TYPE_MOVIE;
        } else {
            return TYPE_LOADING;
        }
    }

    public void clear() {
        this.mMovies = new ArrayList<>();
        notifyDataSetChanged();
        mSize = 0;
    }

    public void addMovies(List<Movie> movies, boolean lastPage) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
        mSize = mMovies.size();
        mLastPage = lastPage;
        if(!lastPage) {
            mSize++;
        }
    }

    // ViewHolders

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_vote)
        TextView vote;
        @BindView(R.id.iv_poster)
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
