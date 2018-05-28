package com.code.red.playvendas.viewmodel;import android.arch.lifecycle.ViewModel;

import android.arch.lifecycle.ViewModelProvider;

import com.code.red.playvendas.viewmodel.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TokenViewModel.class)
    abstract ViewModel bindTokenViewModel(TokenViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}