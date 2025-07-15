package com.broma186.productshopping

import android.app.Application
import com.broma186.productshopping.data.JsonHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import android.util.Log

@HiltAndroidApp
class ProductShoppingApp: Application() {

    @Inject
    lateinit var jsonHelper: JsonHelper

    override fun onCreate() {
        super.onCreate()
        if (!jsonHelper.writeAssetJsonToInternalStorage()) {
            Log.d("DEBUG", "Can't write data to storage, will rely on asset")
        }
    }
}
