package com.broma186.productshopping.data.repository

import com.broma186.productshopping.data.api.ProductService
import com.broma186.productshopping.data.db.ProductDao
import com.broma186.productshopping.data.db.toDomainModel
import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.data.model.mapToEntity
import com.broma186.productshopping.data.model.mapToUI
import com.broma186.productshopping.domain.repository.ProductShoppingRepository
import javax.inject.Inject

class ProductShoppingRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productService: ProductService
): ProductShoppingRepository {

    override suspend fun getProducts(): List<ProductData> {
        val response = productService.getProducts()
        val daoProductsById = productDao.getAllProducts().associateBy { it.id }
        val mergedProducts = response.products.map { productData ->
            val existing = daoProductsById[productData.id]
            val cartCount = existing?.cartCount ?: 0
            productData.mapToEntity(cartCount)
        }
        productDao.insertProducts(mergedProducts)
        return productDao.getAllProducts().map { it.toDomainModel() }
    }

    override suspend fun getProductsLocal(): List<ProductData> {
        val daoProducts = productDao.getAllProducts()
        return daoProducts.map {
            it.toDomainModel()
        }
    }

    override suspend fun getProduct(id: Int): ProductData {
        return productDao.getProduct(id).toDomainModel()
    }

    override suspend fun updateCart(productId: Int, cartCount: Int): Boolean {
        val product = productDao.getProduct(productId)
        val updatedProduct = product.copy(cartCount = cartCount)
        val updatedRows = productDao.updateProduct(updatedProduct)
        return updatedRows > 0
    }

    override suspend fun getCartCount(productId: Int): Int {
        return productDao.getCartCount(productId) ?: 0
    }
}