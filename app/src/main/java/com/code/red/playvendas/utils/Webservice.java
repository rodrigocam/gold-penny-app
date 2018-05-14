package com.code.red.playvendas.utils;

import com.code.red.playvendas.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("product") annotation on the productId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/products/{product}")
    Call<Product> getProduct(@Path("product") int productId);
}