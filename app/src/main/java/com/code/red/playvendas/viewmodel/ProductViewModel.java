package com.code.red.playvendas.viewmodel;

import android.arch.lifecycle.LiveData;
import com.code.red.playvendas.model.Product;

public class ProductViewModel {

    public int productId;
    public LiveData<Product> product;

    public void init(int productId) {
        this.productId = productId;
    }

    public LiveData<Product> getUser() {
        return product;
    }
}
