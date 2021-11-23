package com.danielefavaro.qapital.model.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.danielefavaro.qapital.ext.minusTwoWeeks
import com.danielefavaro.qapital.ext.plusTwoWeeks
import com.danielefavaro.qapital.ext.toApiFormat
import com.danielefavaro.qapital.viewmodel.model.ActivityFeedUI
import com.danielefavaro.qapital.viewmodel.model.ActivityListUI
import com.danielefavaro.qapital_network.model.entity.UserModel
import com.danielefavaro.qapital_network.model.service.QapitalApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * Network source for paging library
 * Needs to be updated replacing Date with Int
 */
class ActivityFeedsPagingSource @Inject constructor(
    private val service: QapitalApi
) : RxPagingSource<Date, ActivityFeedUI>() {

    private val to: Date = Calendar.getInstance().time

    override fun getRefreshKey(state: PagingState<Date, ActivityFeedUI>): Date = to

    override fun loadSingle(params: LoadParams<Date>): Single<LoadResult<Date, ActivityFeedUI>> {
        val position: Date = params.key ?: to
        return service.getActivityFeeds(from = position.plusTwoWeeks().toApiFormat(), to = position.toApiFormat())
            .subscribeOn(Schedulers.io())
            .map { activityFeedList ->
                ActivityListUI(activityFeedList.oldest, activityFeedList.activities.map { feedModel ->
                    val user: UserModel? = try {
                        getUser(feedModel.userId).blockingGet()
                    } catch (_: Exception) {
                        null
                    }

                    ActivityFeedUI(
                        avatarUrl = user?.avatarUrl,
                        message = feedModel.message,
                        amount = feedModel.amount,
                        date = feedModel.timestamp.toString()
                    )
                })
            }
            .map { activityFeedList ->
                LoadResult.Page(
                    data = activityFeedList.activities,
                    prevKey = if (position.time == to.time) null else position.minusTwoWeeks(),
                    nextKey = if (position.time <= activityFeedList.oldest.time) null else position.plusTwoWeeks()
                )
            }
        // TODO doOnError
    }

    private fun getUser(userId: Long): Single<UserModel> = service.getUser(userId)
        .subscribeOn(Schedulers.io())
}