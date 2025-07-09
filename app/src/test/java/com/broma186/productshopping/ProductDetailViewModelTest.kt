package com.broma186.productshopping

import androidx.lifecycle.SavedStateHandle
import com.broma186.productshopping.ProductsViewModelTest.Companion.products
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.domain.usecase.GetCartCountUseCase
import com.broma186.productshopping.domain.usecase.GetProductUseCase
import com.broma186.productshopping.domain.usecase.UpdateCartUseCase
import com.broma186.productshopping.presentation.model.ErrorState
import com.broma186.productshopping.presentation.viewmodel.ProductDetailsViewModel
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    lateinit var mockGetProductUseCase: GetProductUseCase

    @RelaxedMockK
    lateinit var mockUpdateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var mockGetCartCountUseCase: GetCartCountUseCase

    private lateinit var viewModel: ProductDetailsViewModel

    private val productId = 1

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        val savedStateHandle = SavedStateHandle(mapOf("productId" to productId))
        viewModel = ProductDetailsViewModel(
            savedStateHandle,
            mockGetProductUseCase,
            mockUpdateCartUseCase,
            mockGetCartCountUseCase
        )
        coEvery { mockGetProductUseCase.invoke(productId) } returns products[0]
        coEvery { mockGetCartCountUseCase.invoke(productId) } returns 1
    }

    @Test
    fun `when data is returned from the use case, the product are shown in the state, and no error is thrown`() = runTest {


            viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
            advanceUntilIdle()
            assertEquals(viewModel.uiState.value.product, products[0].mapToUI())
            assertEquals(
                viewModel.uiState.value.product?.cartCount,
                products[0].mapToUI().cartCount
            )
        }

    @Test
    fun `when an exception is thrown by calling get products, the error state is set`() = runTest {
        coEvery { mockGetProductUseCase.invoke(productId) } throws Exception()
        viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
        advanceUntilIdle()
        assert(viewModel.uiState.value.error is ErrorState.Fail)
    }

    @Test
    fun `when an exception is thrown by calling get cart count, the error state is set`() = runTest {
        coEvery { mockGetCartCountUseCase.invoke(productId) } throws Exception()
        viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
        advanceUntilIdle()
        assert(viewModel.uiState.value.error is ErrorState.Fail)
    }

    @Test
    fun `when the cart count is updated, it returns true and updated the cart count`() = runTest {
        coEvery { mockUpdateCartUseCase.invoke(productId, 4) } returns true
        viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
        advanceUntilIdle()
        assert(viewModel.uiState.value.product?.cartCount == 1)

        viewModel.onAddToCart(4)
        advanceUntilIdle()
        assert(viewModel.uiState.value.product?.cartCount == 4)
    }

    @Test
    fun `when the cart count update fails, it returns false`() = runTest {
        coEvery { mockUpdateCartUseCase.invoke(productId, 2) } returns false
        viewModel.onIntent(ProductDetailsViewModel.ProductIntent.FetchProduct)
        advanceUntilIdle()
        assert(viewModel.uiState.value.product?.cartCount == 1)

        viewModel.onAddToCart(2)
        advanceUntilIdle()
        assert(viewModel.uiState.value.product?.cartCount == 1)
    }
}

