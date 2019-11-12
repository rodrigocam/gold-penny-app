package com.redcode.goldpenny.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.redcode.goldpenny.model.Product;
import com.redcode.goldpenny.model.Token;
import com.redcode.goldpenny.repository.ProductRepository;

import java.util.List;

import javax.inject.Inject;

public class ProductViewModel extends ViewModel {

    public LiveData<List<Product>> products;
    private ProductRepository productRepository;

    @Inject
    public ProductViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void init(Token token) {
        if (this.products == null) {
            products = productRepository.getProducts(token);
        }
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
