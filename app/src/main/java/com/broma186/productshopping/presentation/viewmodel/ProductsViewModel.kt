package com.broma186.productshopping.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.presentation.model.Product
import com.broma186.productshopping.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState

    private val _backEvent = MutableSharedFlow<ProductIntent>()
    val backEvent: SharedFlow<ProductIntent> = _backEvent

    fun onIntent(intent: ProductIntent) {
        when (intent) {
            ProductIntent.FetchProducts -> fetchData()
            ProductIntent.RefreshProducts -> refreshData()
            ProductIntent.GoBack -> goBack()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _uiState.value = ProductsState(isLoading = true)
                val productList = getProductsUseCase.invoke().mapToUI().filter {
                    it.available && it.inventory > 0
                }
                if (productList.isNotEmpty()) {
                    _uiState.value = ProductsState(products = productList)
                } else {
                    _uiState.value = ProductsState(error = "No content to display")
                }
            } catch (exception: Exception) {
                _uiState.value = ProductsState(error = exception.cause?.message)
            }
        }
    }

    private fun refreshData() {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        fetchData()
        _uiState.value = _uiState.value.copy(isRefreshing = false)
    }

    private fun goBack() {
        viewModelScope.launch {
            _backEvent.emit(ProductIntent.GoBack)
        }
    }

    sealed class ProductIntent {
        data object FetchProducts : ProductIntent()
        data object RefreshProducts : ProductIntent()
        data object GoBack : ProductIntent()
    }

    data class ProductsState(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val products: List<Product> = emptyList(),
        val error: String? = null
    )
}