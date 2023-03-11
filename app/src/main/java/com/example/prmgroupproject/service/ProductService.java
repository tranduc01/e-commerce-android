package com.example.prmgroupproject.service;

import com.example.prmgroupproject.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {
    @GET("product")
    Call<List<Product>> getProducts();
}
