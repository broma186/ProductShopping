package com.broma186.productshopping.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.domain.usecase.GetCartCountUseCase
import com.broma186.productshopping.domain.usecase.GetProductUseCase
import com.broma186.productshopping.domain.usecase.UpdateCartUseCase
import com.broma186.productshopping.presentation.model.ErrorState
import com.broma186.productshopping.presentation.model.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductUseCase: GetProductUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val getCartCountUseCase: GetCartCountUseCase
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState

    fun onIntent(intent: ProductIntent) {
        when (intent) {
            ProductIntent.FetchProduct -> fetchProduct()
        }
    }

    private fun fetchProduct() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val product = getProductUseCase.invoke(id = productId)?.mapToUI()
                val cartCount = getCartCountUseCase.invoke(id = productId)
                _uiState.value = _uiState.value.copy(product = product, cartCount = cartCount, isLoading = false, error = null)
            } catch (exception: Exception) {
               _uiState.value = _uiState.value.copy(error = ErrorState.Fail(exception.cause?.message), isLoading = false)
            }
        }
    }

    suspend fun onAddToCart(cartCount: Int): Boolean {
          return try {
                if (updateCartUseCase.invoke(productId, cartCount)) {
                    _uiState.value = _uiState.value.copy(product = _uiState.value.product?.copy(cartCount = cartCount))
                    return true
                }
              false
            } catch (exception: Exception) {
              false
            }
    }

    sealed class ProductIntent {
        data object FetchProduct : ProductIntent()
    }
}