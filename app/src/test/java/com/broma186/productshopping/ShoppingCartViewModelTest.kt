package com.broma186.productshopping

import com.broma186.productshopping.ProductsViewModelTest.Companion.products
import com.broma186.productshopping.domain.usecase.ClearCartUseCase
import com.broma186.productshopping.domain.usecase.GetProductsLocalUseCase
import com.broma186.productshopping.domain.usecase.UpdateCartUseCase
import com.broma186.productshopping.presentation.viewmodel.ProductsViewModel
import com.broma186.productshopping.presentation.viewmodel.ShoppingCartViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShoppingCartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    lateinit var mockGetProductsLocalUseCase: GetProductsLocalUseCase

    @RelaxedMockK
    lateinit var mockUpdateCartUseCase: UpdateCartUseCase

    @RelaxedMockK
    lateinit var mockClearCartUseCase: ClearCartUseCase

    private lateinit var viewModel: ShoppingCartViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ShoppingCartViewModel(
            mockGetProductsLocalUseCase,
            mockUpdateCartUseCase,
            mockClearCartUseCase
        )
        coEvery { mockGetProductsLocalUseCase.invoke() } returns products
    }

    @Test
    fun `when the products are returns from the local use case, only two products are shown`() = runTest {
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()
        assert(viewModel.uiState.value.error == null)
        assertEquals(viewModel.uiState.value.products.size, 2) // one of the three has no cartCount
    }

    @Test
    fun `updating the cart updates the cart as expected`() = runTest {
        coEvery { mockUpdateCartUseCase.invoke(any(), any()) } returns true
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()

        assert(viewModel.uiState.value.products[0].cartCount == 1) // product 1 has cartCount of 1

        val productId = 1
        val cartCountToUpdateTo = 4
        val paramId = slot<Int>()
        val paramCount = slot<Int>()

        viewModel.updateCart(productId, cartCountToUpdateTo)

        coVerify { mockUpdateCartUseCase.invoke(capture(paramId), capture(paramCount)) }
        assertTrue(paramId.captured == productId)
        assertTrue(paramCount.captured == cartCountToUpdateTo)

        assert(viewModel.uiState.value.products[0].cartCount == cartCountToUpdateTo)
    }

    @Test
    fun `test clearing of cart data`() = runTest {
        coEvery { mockClearCartUseCase.invoke() } returns true
        viewModel.onIntent(ProductsViewModel.ProductsIntent.FetchProducts)
        advanceUntilIdle()

        coEvery { mockGetProductsLocalUseCase.invoke() } returns emptyList()
        viewModel.clearCart()
        advanceUntilIdle()

        coVerify { mockClearCartUseCase.invoke() }
        assertTrue(viewModel.uiState.value.products.isEmpty())
    }
}