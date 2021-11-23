package com.danielefavaro.qapital.model.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.danielefavaro.qapital.model.source.ActivityFeedsSourceMediator
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ActivityFeedsRepository @Inject constructor(
    private val source: ActivityFeedsSourceMediator,
) {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    fun getActivityFeeds(): Flowable<PagingData<ActivityFeedModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            prefetchDistance = 5
        ),
        remoteMediator = source,
        pagingSourceFactory = { source.getAllLocalData() }
    ).flowable
}