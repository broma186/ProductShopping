package com.broma186.productshopping.data.di

import android.content.Context
import com.broma186.productshopping.data.JsonHelper
import com.broma186.productshopping.data.JsonHelper.Companion.GET
import com.broma186.productshopping.data.JsonHelper.Companion.POST
import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.data.model.ProductsResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LocalJsonInterceptor(
    private val context: Context,
    private val jsonHelper: JsonHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.method
        val path = request.url.encodedPath
        var json = ""
        when (method) {
            GET -> when (request.url.encodedPath) {
                "/products" -> {
                    json = jsonHelper.readJson()
                }

                else -> {
                    json = jsonHelper.readJson()
                    val id = path.removePrefix("/products/").toInt()
                    val gson = Gson().fromJson(json, ProductsResponse::class.java)
                    json = Gson().toJson(gson.products[id - 1], ProductData::class.java)
                }
            }

            POST -> when (request.url.encodedPath) {
                "/clearCart" -> { // Clear cart all products.
                    json = jsonHelper.readJson()
                    val gson = Gson().fromJson(json, ProductsResponse::class.java)
                    val clearedJson = gson.products.map {
                        it.copy(
                            cartCount = 0
                        )
                    }
                    json = Gson().toJson(ProductsResponse(clearedJson))
                    jsonHelper.writeToFile(json)
                }
                else -> {  // Update cart specific product.
                    if (request.url.pathSegments.size == 3) {
                        json = jsonHelper.readJson()
                        val productId = request.url.pathSegments[1].toInt()
                        val newCartCount = request.url.pathSegments[2].toInt()
                        val updatedJson = Gson().fromJson(json, ProductsResponse::class.java).products.map {
                            if (it.id == productId) {
                                it.copy(
                                    cartCount = newCartCount
                                )
                            } else {
                                it
                            }
                        }
                        jsonHelper.writeToFile(Gson().toJson(ProductsResponse(updatedJson)))
                        json = Gson().toJson(updatedJson[productId - 1], ProductData::class.java)
                    } else {
                        // no op
                    }
                }
            }
            else -> {
                if (jsonHelper.readJson().isEmpty()) {
                    json = context.assets.open("data.json").bufferedReader().use { it.readText() }
                }
            }
        }
        return Response.Builder()
            .code(200)
            .message(json)
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .body(json.toResponseBody("application/json".toMediaType()))
            .build()
    }
}