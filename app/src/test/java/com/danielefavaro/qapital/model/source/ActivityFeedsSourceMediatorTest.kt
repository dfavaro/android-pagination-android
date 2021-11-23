package com.danielefavaro.qapital.model.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.danielefavaro.qapital.util.TestCoroutineRule
import com.danielefavaro.qapital_database.AppDatabase
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel
import com.danielefavaro.qapital_network.model.entity.ActivityListModel
import com.danielefavaro.qapital_network.model.service.QapitalApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoroutinesApi
class ActivityFeedsSourceMediatorTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Inject
    lateinit var appDatabase: AppDatabase

    private var qapitalApi: QapitalApi = mockk()

    lateinit var remoteMediator: ActivityFeedsSourceMediator

    @Before
    fun setUp() {
        hiltRule.inject()
        remoteMediator = ActivityFeedsSourceMediator(qapitalApi, appDatabase)

        // Need to mock runInTransaction method cause it returns UndeliverableException -> need investigation about it
        remoteMediator = spyk(remoteMediator)
        every { remoteMediator.insertToDb(any(), any(), any(), any()) } returns mockk()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() {
        mockApi()
        val pagingState = PagingState<Int, ActivityFeedModel>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.loadSingle(LoadType.REFRESH, pagingState).blockingGet()
        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached.not())
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() {
        unmockApi()
        val pagingState = PagingState<Int, ActivityFeedModel>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.loadSingle(LoadType.REFRESH, pagingState).blockingGet()
        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    private fun unmockApi() {
        val mockDate: Date = mockk()
        every { mockDate.time } returns 0L

        every { qapitalApi.getActivityFeeds(any(), any()) } returns Single.just(ActivityListModel(mockDate, emptyList()))
    }

    private fun mockApi() {
        val mockDate: Date = mockk()
        every { mockDate.time } returns 0L

        every { qapitalApi.getActivityFeeds(any(), any()) } returns Single.just(
            ActivityListModel(
                mockDate, listOf(
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 6",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 5",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 4",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 3",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 2",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 1",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                    com.danielefavaro.qapital_network.model.entity.ActivityFeedModel(
                        message = "Message 0",
                        timestamp = mockk(),
                        amount = 0.0,
                        userId = 0L
                    ),
                )
            )
        )
    }
}