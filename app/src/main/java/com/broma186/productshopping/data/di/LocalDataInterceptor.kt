package com.broma186.productshopping.data.di

import android.content.Context
import com.broma186.productshopping.data.JsonHelper
import com.broma186.productshopping.data.JsonHelper.Companion.GET
import com.broma186.productshopping.data.JsonHelper.Companion.POST
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
        var json = ""
        when (method) {
            GET -> {
                json = jsonHelper.readJson()
            }

            POST -> {
                val buffer = okio.Buffer()
                request.body?.writeTo(buffer)
                val newJson = buffer.readUtf8()
                jsonHelper.writeToFile(newJson)
                json = newJson
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