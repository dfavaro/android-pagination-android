package com.danielefavaro.qapital_network

import com.danielefavaro.qapital_network.ext.fromApiFormat
import com.danielefavaro.qapital_network.model.entity.ActivityListModel
import com.danielefavaro.qapital_network.model.entity.UserModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@ExperimentalCoroutinesApi
class NetworkTest : BaseNetworkTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    override fun setUp() {
        hiltRule.inject()
        super.setUp()
    }

    @Test
    fun getActivityFeeds() = runBlocking {
        val response: ActivityListModel = api.getActivityFeeds("2016-10-03T00:00:00+00:00", "2016-10-04T00:00:00+00:00").blockingGet()
        assert(response.oldest == "2016-05-23T02:00:00+00:00".fromApiFormat())
        assert(response.activities[0].userId == 2L)
        assert(response.activities[1].userId == 3L)
    }

    @Test
    fun getUser() = runBlocking {
        val response: UserModel = api.getUser(1L).blockingGet()
        assert(response.userId == 1L)
    }
}