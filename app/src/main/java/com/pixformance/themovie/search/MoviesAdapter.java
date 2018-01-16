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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter {

    List<Movie> mMovies;

    OnSelectMovie onSelectMovie;

    public void setOnSelectMovie(OnSelectMovie onSelectMovie) {
        this.onSelectMovie = onSelectMovie;
    }

    interface OnSelectMovie {
        void onSelectMovie(Movie movie);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
                onSelectMovie.onSelectMovie(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

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
}
