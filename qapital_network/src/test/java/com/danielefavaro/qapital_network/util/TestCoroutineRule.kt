package com.danielefavaro.qapital_network.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestCoroutineRule constructor(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}

@ExperimentalCoroutinesApi
fun TestCoroutineRule.runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
    testDispatcher.runBlockingTest(block)

@ExperimentalCoroutinesApi
suspend fun TestCoroutineRule.pauseDispatcher(block: suspend () -> Unit) =
    testDispatcher.pauseDispatcher(block)

@ExperimentalCoroutinesApi
fun TestCoroutineRule.runCurrent() =
    testDispatcher.runCurrent()

@ExperimentalCoroutinesApi
fun TestCoroutineRule.resumeDispatcher() =
    testDispatcher.resumeDispatcher()