package com.broma186.productshopping.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.domain.usecase.GetProductsUseCase
import com.broma186.productshopping.presentation.model.ErrorState
import com.broma186.productshopping.presentation.model.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState

    fun onIntent(intent: ProductsIntent) {
        when (intent) {
            ProductsIntent.FetchProducts -> fetchData(false)
            ProductsIntent.RefreshProducts -> fetchData(true)
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _uiState.value = ProductsState(isLoading = !isRefresh, isRefreshing = isRefresh, error = null)
                val productList = getProductsUseCase.invoke().map { it.mapToUI() }.filter {
                    it.available && it.inventory > 0
                }
                if (productList.isNotEmpty()) {
                    _uiState.value = ProductsState(products = productList, isLoading = false, isRefreshing = false)
                } else {
                    _uiState.value = ProductsState(error = ErrorState.NoData, isLoading = false, isRefreshing = false)
                }
            } catch (exception: Exception) {
                _uiState.value = ProductsState(error = ErrorState.Fail(exception.cause?.message), isLoading = false, isRefreshing = false)
            }
        }
    }

    sealed class ProductsIntent {
        data object FetchProducts : ProductsIntent()
        data object RefreshProducts : ProductsIntent()
    }
}