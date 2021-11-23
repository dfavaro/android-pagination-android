package com.danielefavaro.qapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.cachedIn
import com.danielefavaro.qapital.model.repo.ActivityFeedsRepository
import com.danielefavaro.qapital.viewmodel.model.ActivityFeedUI
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class ActivityFeedViewModel @Inject constructor(
    private val repository: ActivityFeedsRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedList: Flowable<PagingData<ActivityFeedUI>> by lazy {
        repository
            .getActivityFeeds()
            .onErrorComplete() // TODO Handle the error as needed (displaying an error dialog, etc...)
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