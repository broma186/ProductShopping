package com.broma186.productshopping.data

import android.content.Context
import java.io.File

class JsonHelper(private val context: Context) {


    companion object {
        const val FILE_NAME = "product_shopping_data.json"
        const val GET = "GET"
        const val POST = "POST"
    }

    fun writeAssetJsonToInternalStorage(): Boolean {
        return try {
            val file = File(context.filesDir, FILE_NAME)
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

    fun writeToFile(json: String): Boolean {
        return try {
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readJson(): String {
        return try {
            val fileToRead = File(context.filesDir, FILE_NAME)
             if (fileToRead.exists() ) {
                fileToRead.readText()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
}