package com.redcode.goldpenny;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.redcode.goldpenny.room.fixed.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends Application implements HasActivityInjector {

    @Inject
    public DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return this.dispatchingAndroidInjector;
    }

    private void initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
