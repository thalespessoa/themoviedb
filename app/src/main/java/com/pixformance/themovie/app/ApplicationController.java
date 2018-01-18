package com.pixformance.themovie.app;

import android.app.Application;
import android.app.Fragment;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;

/**
 * Main app class. This setups applications modules and start routines
 */
public class ApplicationController extends Application {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ApplicationComponent applicationComponent;

	@Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .build();
    }

    public ApplicationComponent getAppComponent() {
        return applicationComponent;
    }
}
