package com.broma186.productshopping.data.repository

import com.broma186.productshopping.data.api.ProductService
import com.broma186.productshopping.data.db.ProductDao
import com.broma186.productshopping.data.db.toDomainModel
import com.broma186.productshopping.data.model.ProductData
import com.broma186.productshopping.data.model.mapToEntity
import com.broma186.productshopping.domain.repository.ProductShoppingRepository
import javax.inject.Inject

class ProductShoppingRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productService: ProductService
): ProductShoppingRepository {

    override suspend fun getProducts(): List<ProductData> {
        val response = productService.getProducts()
        productDao.insertProducts(response.mapToEntity())
        return productDao.getAllProducts().map { it.toDomainModel() }
    }

    override suspend fun getProduct(id: Int): ProductData {
        return productDao.getProduct(id).toDomainModel()
    }
}