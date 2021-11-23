package com.danielefavaro.qapital_network.di

import com.danielefavaro.qapital_network.model.service.QapitalApi
import com.danielefavaro.qapital_network.util.WebServerConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
class MockNetworkModule {

    @Provides
    fun provideApi(okHttpClient: OkHttpClient, serverConfig: WebServerConfig): QapitalApi = Retrofit.Builder()
        .baseUrl("http://localhost:${serverConfig.port}")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(QapitalApi::class.java)

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideMockedServer(): MockWebServer = MockWebServer()

    @Singleton
    @Provides
    fun provideMockedServerConfig(): WebServerConfig = WebServerConfig(9999)

    @Singleton
    @Provides
    @SuccessDispatcher
    fun provideSuccessDispatcher(): Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return with(request.path!!) {
                when {
                    contains("activities") -> MockResponse().setResponseCode(200).setBody(getJsonFromFile("activityListModel.json"))
                    contains("users") -> MockResponse().setResponseCode(200).setBody(getJsonFromFile("userModel.json"))
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    fun getJsonFromFile(filename: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream =
                javaClass.classLoader?.getResourceAsStream(filename)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SuccessDispatcher