package com.broma186.productshopping.data.di

import android.content.Context
import androidx.room.Room
import com.broma186.productshopping.data.JsonHelper
import com.broma186.productshopping.data.api.ProductService
import com.broma186.productshopping.data.db.ProductDao
import com.broma186.productshopping.data.db.ProductDatabase
import com.broma186.productshopping.data.repository.ProductShoppingRepositoryImpl
import com.broma186.productshopping.domain.repository.ProductShoppingRepository
import com.broma186.productshopping.domain.usecase.ClearCartUseCase
import com.broma186.productshopping.domain.usecase.GetCartCountUseCase
import com.broma186.productshopping.domain.usecase.GetProductUseCase
import com.broma186.productshopping.domain.usecase.GetProductsLocalUseCase
import com.broma186.productshopping.domain.usecase.GetProductsUseCase
import com.broma186.productshopping.domain.usecase.UpdateCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val MOCK_API = "https://mock.api/"
    }

    @Provides
    fun provideLocalJsonInterceptor(@ApplicationContext context: Context, jsonHelper: JsonHelper): LocalJsonInterceptor {
        return LocalJsonInterceptor(context, jsonHelper)
    }

    @Provides
    fun provideOkHttpClient(localJsonInterceptor: LocalJsonInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(localJsonInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ProductService {
        return Retrofit.Builder()
            .baseUrl(MOCK_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductShoppingDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, "product_db").build()
    }

    @Provides
    fun provideProductShoppingDao(db: ProductDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    fun provideJsonHelper(@ApplicationContext context: Context): JsonHelper {
        return JsonHelper(context)
    }

    @Provides
    @Singleton
    fun provideProductShoppingRepository(productShoppingDao: ProductDao, productService: ProductService): ProductShoppingRepository {
        return ProductShoppingRepositoryImpl(productShoppingDao, productService)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productShoppingRepository: ProductShoppingRepository): GetProductsUseCase {
        return GetProductsUseCase(productShoppingRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsLocalUseCase(productShoppingRepository: ProductShoppingRepository): GetProductsLocalUseCase {
        return GetProductsLocalUseCase(productShoppingRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductUseCase(productShoppingRepository: ProductShoppingRepository): GetProductUseCase {
        return GetProductUseCase(productShoppingRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateCartUseCase(productShoppingRepository: ProductShoppingRepository): UpdateCartUseCase {
        return UpdateCartUseCase(productShoppingRepository)
    }

    @Provides
    @Singleton
    fun provideGetCartCountUseCase(productShoppingRepository: ProductShoppingRepository): GetCartCountUseCase {
        return GetCartCountUseCase(productShoppingRepository)
    }

    @Provides
    @Singleton
    fun provideClearCartUseCase(productShoppingRepository: ProductShoppingRepository): ClearCartUseCase {
        return ClearCartUseCase(productShoppingRepository)
    }
}