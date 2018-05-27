package com.code.red.playvendas.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.repository.ProductRepository;

import javax.inject.Inject;

public class ProductViewModel extends ViewModel {

    public LiveData<Product> product;
    private ProductRepository productRepository;

    @Inject
    public ProductViewModel(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public void init() {
        if(this.product == null){
            product = productRepository.getProduct(0);
        }
    }

    public LiveData<Product> getProduct() {
        return product;
    }
}
