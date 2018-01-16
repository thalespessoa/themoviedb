package com.pixformance.themovie.data;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thalespessoa on 1/16/18.
 */

public abstract class ServiceCallback<T> implements Callback<T> {

    public abstract void onSuccess(Headers headers, T response);
//    public abstract void onError(HttpException exception);
    public abstract void onError(String exception);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            onSuccess(response.headers(), response.body());
        } else {
            onError("error");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError("error");
    }
}
