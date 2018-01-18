package com.pixformance.themovie.util;

import android.content.Context;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.HttpException;
import com.pixformance.themovie.data.model.Movie;

/**
 * Created by thalespessoa on 1/17/18.
 */

public class TextUtil {
    static public String formatAvarageVote(Movie movie) {
        float vote = movie.getVoteAverage();
        int numberInt = (int) vote;
        if (movie.getVoteCount() == 0) {
            return "-";
        } else if (numberInt == vote) {
            return String.valueOf(numberInt);
        } else {
            return String.valueOf(vote);
        }
    }

    static public String getErrorMessage(Context context, int code) {
        if(code == HttpException.ERROR_EMPTY) {
            return context.getResources().getString(R.string.no_movie_found);
        } else if(code == HttpException.ERROR_SERVER) {
            return context.getResources().getString(R.string.internet_error);
        } else {
            return context.getResources().getString(R.string.generic_error);
        }
    }
}
