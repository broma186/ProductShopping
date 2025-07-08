package com.broma186.productshopping.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.domain.usecase.ClearCartUseCase
import com.broma186.productshopping.domain.usecase.GetProductsLocalUseCase
import com.broma186.productshopping.domain.usecase.UpdateCartUseCase
import com.broma186.productshopping.presentation.model.ProductsState
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel.ProductsIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getProductsLocalUseCase: GetProductsLocalUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState

    fun onIntent(intent: ProductsIntent) {
        when (intent) {
            ProductsIntent.FetchProducts -> fetchData()
            ProductsIntent.RefreshProducts -> {
                // no op
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _uiState.value = ProductsState(isLoading = true)
                val productList = getProductsLocalUseCase.invoke().filter {
                    (it.cartCount ?: 0) > 0
                }.map {
                    it.mapToUI()
                }
                if (productList.isNotEmpty()) {
                    _uiState.value = ProductsState(products = productList, isLoading = false)
                } else {
                    _uiState.value = ProductsState(error = "No content to display", isLoading = false)
                }
            } catch (exception: Exception) {
                _uiState.value = ProductsState(error = exception.cause?.message, isLoading = false)
            }
        }
    }

    suspend fun updateCart(productId: Int, cartCount: Int): Boolean {
        return try {
            if (updateCartUseCase.invoke(productId, cartCount)) {
                val updatedProducts = _uiState.value.products
                    .map { product ->
                        if (product.id == productId) product.copy(cartCount = cartCount) else product
                    }
                    .filter { it.cartCount > 0 }

                _uiState.value = _uiState.value.copy(
                    products = updatedProducts,
                    error = if (updatedProducts.isEmpty()) "No content to display" else null
                )
                return true
            }
            false
        } catch (exception: Exception) {
            false
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            if (clearCartUseCase.invoke()) {
                fetchData()
            }
        }
    }

}