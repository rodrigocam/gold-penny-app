package com.code.red.playvendas.utils;

import com.code.red.playvendas.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("product") annotation on the productId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("http://159.89.140.211/api/v1/products/")
    Call<List<Product>> getProducts(@Header("Authorization") String token);


    //@GET("http://206.189.123.66/api/v1/products")
    //Call<Product> getProduct(@Header("Authorization") String token);

}