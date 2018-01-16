package com.pixformance.themovie.module;

import com.pixformance.themovie.search.SearchListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by thalespessoa on 1/15/18.
 */

@Module
public abstract class AppModule {
    @ContributesAndroidInjector
    abstract SearchListFragment contributeActivityInjector();
}
