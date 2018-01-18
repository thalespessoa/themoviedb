package com.pixformance.themovie.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.NetworkApi;
import com.pixformance.themovie.data.model.Movie;
import com.pixformance.themovie.util.TextUtil;
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

    /**
     * Callback to handle movie selections and 'next page' call
     * @param moviesAdapterCallback
     */
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
        System.out.println("MoviesAdapter.onBindViewHolder: "+position);
        if(getItemViewType(position) == TYPE_MOVIE) {
            final Movie movie = mMovies.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            movieViewHolder.title.setText(movie.getTitle());
            movieViewHolder.vote.setText(TextUtil.formatAvarageVote(movie));
            Picasso.with(movieViewHolder.poster.getContext())
                    .load(String.format(NetworkApi.POSTER_PATH,movie.getPosterPath()))
                    .fit().centerCrop()
                    .into(movieViewHolder.poster);

            if(!movie.getTitle().equals(movie.getOriginalTitle())) {
                movieViewHolder.original.setText(String.format("(%s)", movie.getOriginalTitle()));
                movieViewHolder.original.setVisibility(View.VISIBLE);
            } else {
                movieViewHolder.original.setVisibility(View.GONE);
            }

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

    /**
     * Clean list
     */
    public void clear() {
        this.mMovies = new ArrayList<>();
        notifyDataSetChanged();
        mSize = 0;
    }

    /**
     * Add more movies on the list
     * @param movies
     * @param lastPage
     */
    public void addMovies(List<Movie> movies, boolean lastPage) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
        mSize = mMovies.size();
        mLastPage = lastPage;
        if(!lastPage) {
            mSize++;
        }
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    // ViewHolders

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_original_title)
        TextView original;
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
