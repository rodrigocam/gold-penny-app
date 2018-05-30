package com.code.red.playvendas.viewmodel;

import com.code.red.playvendas.activities.DisplayProductsActivity;
import com.code.red.playvendas.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();
    @ContributesAndroidInjector
    abstract DisplayProductsActivity contributeDisplayProductsActivity();
}