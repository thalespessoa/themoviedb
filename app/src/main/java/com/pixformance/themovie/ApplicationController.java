package com.pixformance.themovie;

import android.app.Application;
import android.app.Fragment;

import com.pixformance.themovie.module.DataModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

/**
 * Main app class. This setups applications modules and start routines
 */
public class ApplicationController extends Application implements HasFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

	@Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
