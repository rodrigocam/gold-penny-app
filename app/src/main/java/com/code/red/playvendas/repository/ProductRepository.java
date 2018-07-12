package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.model.Token;
import com.code.red.playvendas.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
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

    public LiveData<List<Product>> getProducts(Token token) {
        refreshProducts(token);
        return productDao.loadAll();
    }

    private void refreshProducts(Token token) {
        executor.execute(() -> {
            if (productDao.getProductCount() != 0) {
                productDao.deleteProducts();
            }    // refresh the data

            Response response = null;
            try {
                response = webservice.getProducts("Token "+token.getToken()).execute();
            }catch (IOException e){
                //TODO: Verify what this IOException means here;
                e.printStackTrace();
            }
            List<Product> products = null;
            try{
                Log.d("ResponseBody", response.body().toString());
                products = (List<Product>) response.body();
            }catch (Exception e){
                Log.d("ProductRepository", "Not today, buddy.");
            }
            for(Product product : products){
                Log.d("ReceivedProduct",product.toString());
                product.setApiId(product.getId());
                Log.d("ReceivedProduct",product.toString());
                executor.execute(()->{
                    productDao.save(product);
                });
            }


            // TODO check for error etc.
            // Update the database.The LiveData will automatically refresh so
            // we don't need to do anything else here besides updating the database
            // TODO: Product conversion done  without checks, verify this
        });
    }
}