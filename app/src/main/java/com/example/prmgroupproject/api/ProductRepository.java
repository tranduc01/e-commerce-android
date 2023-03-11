package com.example.prmgroupproject.api;

import com.example.prmgroupproject.service.ProductService;

public class ProductRepository {
    public static ProductService getProductService(){
        return APIClient.getClient().create(ProductService.class);
    }
}
