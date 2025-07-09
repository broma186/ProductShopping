package com.broma186.productshopping

import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.domain.usecase.GetProductsUseCase
import com.broma186.productshopping.presentation.model.ErrorState
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductsViewModelTest {

    companion object {
        val products = listOf(
            ProductData(1, "A", 45.9, "NZD", "Description A", "fence", "Wire fencing", true,  5, 4.5, 2, "unit", 1),
            ProductData(2, "B", 3.56, "NZD", "Description B", "electric_bolt", "Electric Fence", true,  5, 4.5, 2, "unit", 3),
            ProductData(3, "C", 11.99, "NZD", "Description C", "view_column", "Fence Posts", true,  5, 4.5, 2, "unit", 0),
        )
    }

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    lateinit var mockGetProductsUseCase: GetProductsUseCase

    private lateinit var viewModel: ProductsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductsViewModel(mockGetProductsUseCase)
    }

    @Test
    fun `when data is returned from the use case, the products are shown in the state, and no error is thrown`() = runTest {
        coEvery { mockGetProductsUseCase.invoke() } returns products

        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.error != null)
        assertTrue(state.products.isNotEmpty())
        assertEquals(3, state.products.size)
    }

    @Test
    fun `when an exception is thrown after getting the data, the fail error state is set`() = runTest {
        coEvery { mockGetProductsUseCase.invoke() } throws Exception()

        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()

        assert(viewModel.uiState.value.error is ErrorState.Fail)
    }

    @Test
    fun `when the data returns an empty list, the no data error state is set`() = runTest {
        coEvery { mockGetProductsUseCase.invoke() } returns emptyList()

        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.error is ErrorState.NoData)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}