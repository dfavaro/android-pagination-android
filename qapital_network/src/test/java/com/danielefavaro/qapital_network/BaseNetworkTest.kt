package com.danielefavaro.qapital_network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danielefavaro.qapital_network.di.SuccessDispatcher
import com.danielefavaro.qapital_network.model.service.QapitalApi
import com.danielefavaro.qapital_network.util.TestCoroutineRule
import com.danielefavaro.qapital_network.util.WebServerConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseNetworkTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Inject
    lateinit var serverConfig: WebServerConfig

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    @SuccessDispatcher
    lateinit var successDispatcher: Dispatcher

    @Inject
    lateinit var api: QapitalApi

    @Before
    open fun setUp() {
        mockWebServer.start(serverConfig.port)
        mockWebServer.dispatcher = successDispatcher
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
    }
}