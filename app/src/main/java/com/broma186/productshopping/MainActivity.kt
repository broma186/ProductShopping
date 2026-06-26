package com.broma186.productshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.broma186.productshopping.presentation.navigation.AppNavHost
import com.broma186.productshopping.ui.theme.ProductShoppingTheme
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val firebaseModel = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-3.5-flash")
        setContent {
            ProductShoppingTheme {
                 AppNavHost(firebaseModel, rememberNavController())
            }
        }
    }
}