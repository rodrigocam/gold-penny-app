package com.code.red.playvendas.utils;

import android.arch.lifecycle.MutableLiveData;

import com.code.red.playvendas.model.Product;

public class ProductCache {
    public void put(int productId, MutableLiveData<Product> product) {
        // TODO: Simple memory cache, whatever this means...
    }

    public MutableLiveData<Product> get(int productId) {
        return null;
    }
}
