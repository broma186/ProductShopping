package com.broma186.productshopping.data.api

import com.broma186.productshopping.data.model.ProductsResponse
import retrofit2.http.GET

interface ProductService {

    @GET("products")
    suspend fun getProducts(): ProductsResponse
}