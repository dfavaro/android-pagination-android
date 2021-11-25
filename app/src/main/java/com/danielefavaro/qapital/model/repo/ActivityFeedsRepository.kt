package com.danielefavaro.qapital.model.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.danielefavaro.qapital.model.source.ActivityFeedsSourceMediator
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivityFeedsRepository @Inject constructor(
    private val source: ActivityFeedsSourceMediator,
) {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    fun getActivityFeeds(): Flow<PagingData<ActivityFeedModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            prefetchDistance = 5
        ),
        remoteMediator = source,
        pagingSourceFactory = { source.getAllLocalData() }
    ).flow
}