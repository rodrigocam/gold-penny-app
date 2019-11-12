package com.redcode.goldpenny.room.editable;

import com.redcode.goldpenny.activities.DisplayProductsActivity;
import com.redcode.goldpenny.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract DisplayProductsActivity contributeDisplayProductsActivity();
}