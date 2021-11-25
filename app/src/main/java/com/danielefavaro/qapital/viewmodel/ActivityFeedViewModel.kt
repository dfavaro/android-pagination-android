package com.danielefavaro.qapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.danielefavaro.qapital.model.repo.ActivityFeedsRepository
import com.danielefavaro.qapital.viewmodel.model.ActivityFeedUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ActivityFeedViewModel @Inject constructor(
    private val repository: ActivityFeedsRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedList: Flow<PagingData<ActivityFeedUI>> by lazy {
        repository.getActivityFeeds()
            .map {
                it.map { feed ->
                    ActivityFeedUI(
                        avatarUrl = feed.avatarUrl,
                        message = feed.message,
                        date = feed.timestamp,
                        amount = feed.amount
                    )
                }
            }
            .cachedIn(viewModelScope)
    }
}