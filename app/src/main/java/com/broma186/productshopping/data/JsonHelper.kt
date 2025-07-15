package com.broma186.productshopping.data

import android.content.Context
import java.io.File

class JsonHelper(private val context: Context) {

    private val fileName = "product_shopping_data.json"

    fun writeAssetJsonToInternalStorage(): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                val json = context.assets.open("data.json").bufferedReader().use { it.readText() }
                writeToFile(json)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun writeToFile(json: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }
}