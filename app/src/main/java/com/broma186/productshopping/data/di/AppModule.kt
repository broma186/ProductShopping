package com.broma186.productshopping.data.di

import android.content.Context
import com.broma186.productshopping.data.api.ProductService
import com.broma186.productshopping.data.repository.ProductShoppingRepositoryImpl
import com.broma186.productshopping.domain.repository.ProductShoppingRepository
import com.broma186.productshopping.domain.usecase.GetProductsUseCase
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
    fun provideLocalJsonInterceptor(@ApplicationContext context: Context): LocalJsonInterceptor {
        return LocalJsonInterceptor(context)
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
    fun provideProductShoppingRepository(productService: ProductService): ProductShoppingRepository {
        return ProductShoppingRepositoryImpl(productService)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(userInfoRepository: ProductShoppingRepository): GetProductsUseCase {
        return GetProductsUseCase(userInfoRepository)
    }
}