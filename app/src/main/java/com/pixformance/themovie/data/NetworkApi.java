package com.pixformance.themovie.data;

import android.app.Application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixformance.themovie.data.model.SearchResult;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class NetworkApi {

    public static String IMAGES_PATH = "https://image.tmdb.org/t/p/w500/%s";
    private static String API_URL = "https://api.themoviedb.org";
    private static String API_KEY = "2696829a81b1b5827d515ff121700838";

    private Retrofit mRetrofit;
    public NetworkApi() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", API_KEY)
                                .build();

                        Request.Builder requestBuilder = original.newBuilder().url(url);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
        OkHttpClient client = clientBuilder.build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
    }

    public SearchApi createSearchApi() {
        return mRetrofit.create(SearchApi.class);
    }

    public interface SearchApi {
        @GET("/3/search/movie")
        Call<SearchResult> search(@Query("query") String query, @Query("page") int page);
    }
}

