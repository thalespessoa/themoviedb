package com.pixformance.themovie;

import com.pixformance.themovie.module.ApiModule;
import com.pixformance.themovie.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Dagger component to handle dependency injection
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ApiModule.class})
public interface ApplicationComponent extends AndroidInjector<ApplicationController> {
}