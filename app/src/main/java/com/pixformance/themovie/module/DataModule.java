package com.pixformance.themovie.module;

import android.app.Application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixformance.themovie.data.DataSource;
import com.pixformance.themovie.data.LocalStore;
import com.pixformance.themovie.data.NetworkApi;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by thalespessoa on 1/15/18.
 *
 * Dagger module to Pixformance NetworkApi. This class exposes the NetworkApi as Java classes.
 */
@Module
public class DataModule {

    private static String API_URL = "https://api.themoviedb.org";
    private static String API_KEY = "2696829a81b1b5827d515ff121700838";

    private Retrofit mRetrofit;

    public DataModule(Application application) {
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


        Realm.init(application);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Provides
    @Singleton
    public NetworkApi getSearchService() {
        return mRetrofit.create(NetworkApi.class);
    }


    @Provides
    @Singleton
    public LocalStore getSuggestionsPersistence() {
        return new LocalStore();
    }

    @Provides
    @Singleton
    public DataSource getSearchDataSource(NetworkApi networkApi, LocalStore localStore) {
        return new DataSource(networkApi, localStore);
    }
}