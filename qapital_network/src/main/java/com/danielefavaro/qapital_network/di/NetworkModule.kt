package com.danielefavaro.qapital_network.di

import com.danielefavaro.qapital_network.BuildConfig
import com.danielefavaro.qapital_network.model.service.QapitalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
open class NetworkModule {

    protected open val timeout: Long = 20

    @Singleton
    @Provides
    fun provideApi(okHttpClient: OkHttpClient): QapitalApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL.toHttpUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(QapitalApi::class.java)

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .addLogging()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .build()

    private fun OkHttpClient.Builder.addLogging() =
        this.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
            }
        }
}