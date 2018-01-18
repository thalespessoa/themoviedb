package com.pixformance.themovie;

import android.app.Application;
import android.app.Fragment;

import com.pixformance.themovie.module.DataModule;

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

//        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
