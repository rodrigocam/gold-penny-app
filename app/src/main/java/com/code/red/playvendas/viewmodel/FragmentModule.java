package com.code.red.playvendas.viewmodel;

import com.code.red.playvendas.activities.TokenFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract TokenFragment contributeUserProfileFragment();
}
