package com.pixformance.themovie.app;

import android.app.Application;

import com.pixformance.themovie.data.DataProvider;
import com.pixformance.themovie.data.LocalStore;
import com.pixformance.themovie.data.NetworkApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by thalespessoa on 1/15/18.
 */
@Module
public class DataModule {

    Application mApplication;

    public DataModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public LocalStore getLocalStore() {
        return new LocalStore(mApplication);
    }


    @Provides
    @Singleton
    public NetworkApi getNetworkApi() {
        return new NetworkApi();
    }

    @Provides
    @Singleton
    public NetworkApi.SearchApi getSearchService(NetworkApi networkApi) {
        return networkApi.createSearchApi();
    }

    @Provides
    @Singleton
    public DataProvider getSearchDataSource(NetworkApi.SearchApi networkApi, LocalStore localStore) {
        return new DataProvider(networkApi, localStore);
    }
}
