package com.pixformance.themovie.app;

import com.pixformance.themovie.search.SearchListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component to handle dependency injection
 */
@Singleton
@Component(modules = {DataModule.class})
public interface ApplicationComponent {
    void inject(SearchListFragment fragment);
}