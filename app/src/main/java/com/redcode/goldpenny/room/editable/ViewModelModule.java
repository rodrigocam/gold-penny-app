package com.redcode.goldpenny.room.editable;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.redcode.goldpenny.room.fixed.ViewModelFactory;
import com.redcode.goldpenny.room.fixed.ViewModelKey;
import com.redcode.goldpenny.viewmodel.ProductViewModel;
import com.redcode.goldpenny.viewmodel.TokenViewModel;

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