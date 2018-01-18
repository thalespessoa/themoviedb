package com.pixformance.themovie.app;

import android.app.Application;

/**
 * Main app class. This setups applications modules and start routines
 */
public class ApplicationController extends Application {

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
