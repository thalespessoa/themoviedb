package com.pixformance.themovie;

import com.pixformance.themovie.module.DataModule;
import com.pixformance.themovie.search.SearchListFragment;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Dagger component to handle dependency injection
 */
@Singleton
@Component(modules = {DataModule.class})
public interface ApplicationComponent {
    void inject(SearchListFragment fragment);
}