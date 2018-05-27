package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.Webservice;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;


@Singleton
public class ProductRepository {
    private final Webservice webservice;
    private final ProductDao productDao;
    private final Executor executor;

    @Inject
    public ProductRepository(Webservice webservice, ProductDao productDao, Executor executor) {
        this.webservice = webservice;
        this.productDao = productDao;
        this.executor = executor;
    }

    public LiveData<Product> getProduct(int productId) {
        refreshProduct(productId);
        // return a LiveData directly from the database.
        return productDao.load(productId);
    }

    private void refreshProduct(final int productId) {
        executor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently

            // TODO: Verify what the hell this was.
            //boolean userExists = productDao.hasUser(FRESH_TIMEOUT);
            boolean userExists = true;
            if (!userExists) {
                // refresh the data

                Response response = null;
                try {
                    response = webservice.getProduct(productId).execute();
                }catch (IOException e){
                    //TODO: Verify what this IOException means here;
                    e.printStackTrace();
                }

                productDao.save((Product)response.body());


                // TODO check for error etc.
                // Update the database.The LiveData will automatically refresh so
                // we don't need to do anything else here besides updating the database
                // TODO: Product conversion done  without checks, verify this
            }
        });
    }
}