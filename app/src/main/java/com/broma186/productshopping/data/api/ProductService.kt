package com.broma186.productshopping.data.api

import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.data.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    suspend fun getProducts(): ProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductData?

    @POST("updateCart/{id}/{cartCount}")
    suspend fun updateProduct(@Path("id") id: Int, @Path("cartCount") cartCount: Int): ProductData

    @POST("clearCart")
    suspend fun clearProducts(): ProductsResponse
}