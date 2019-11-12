package com.redcode.goldpenny.utils;

import com.redcode.goldpenny.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Webservice {

    @GET(RequestBuilder.SERVER_URL +"api/v1/products/")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

}