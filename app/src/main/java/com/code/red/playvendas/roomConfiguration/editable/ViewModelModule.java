package com.code.red.playvendas.roomConfiguration.editable;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.code.red.playvendas.roomConfiguration.fixed.ViewModelFactory;
import com.code.red.playvendas.roomConfiguration.fixed.ViewModelKey;
import com.code.red.playvendas.viewmodel.ProductViewModel;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TokenViewModel.class)
    abstract ViewModel bindTokenViewModel(TokenViewModel tokenViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}