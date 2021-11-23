package com.danielefavaro.qapital.model.source

import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.danielefavaro.qapital.ext.minusTwoWeeks
import com.danielefavaro.qapital.ext.plusTwoWeeks
import com.danielefavaro.qapital.ext.toApiFormat
import com.danielefavaro.qapital_database.AppDatabase
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel
import com.danielefavaro.qapital_database.model.entity.ActivityFeedRemoteKeys
import com.danielefavaro.qapital_network.model.entity.ActivityListModel
import com.danielefavaro.qapital_network.model.entity.UserModel
import com.danielefavaro.qapital_network.model.service.QapitalApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * Network and memory source for paging library
 */
@OptIn(ExperimentalPagingApi::class)
class ActivityFeedsSourceMediator @Inject constructor(
    private val service: QapitalApi,
    private val database: AppDatabase
) : RxRemoteMediator<Int, ActivityFeedModel>() {

    private val to: Date = Calendar.getInstance().time

    override fun loadSingle(loadType: LoadType, state: PagingState<Int, ActivityFeedModel>): Single<MediatorResult> = Single.just(loadType)
        .subscribeOn(Schedulers.io())
        .doOnError { MediatorResult.Error(it) }
        .map {
            when (it) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.plusTwoWeeks() ?: to
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }
        }
        .flatMap { loadTypeResult ->
            /**
             * @param loadTypeResult can be a
             *  - MediatorResult object based on loadType when statement result or
             *  - the current position date, which could be updated later when retrieving a list of empty feeds
             */
            (loadTypeResult as? MediatorResult)?.let { result ->
                return@flatMap Single.just(result)
            }

            return@flatMap getActivityListModel(loadTypeResult as Date)
                .map { (updatedPosition, activityFeedList) ->
                    Triple(updatedPosition, activityFeedList.oldest, activityFeedList.activities.map { feedModel ->
                        val user: UserModel? = try {
                            getUser(feedModel.userId).blockingGet()
                        } catch (_: Exception) {
                            null
                        }

                        ActivityFeedModel(
                            avatarUrl = user?.avatarUrl,
                            displayName = user?.displayName,
                            userId = feedModel.userId,
                            message = feedModel.message,
                            amount = feedModel.amount,
                            timestamp = feedModel.timestamp.toString()
                        )
                    })
                }
                .map { (updatedPosition, oldest, feeds) ->
                    val endOfPagination: Boolean = updatedPosition.time <= oldest.time
                    insertToDb(loadType, updatedPosition, feeds, endOfPagination)
                    MediatorResult.Success(endOfPaginationReached = endOfPagination)
                }
                .doOnError { throwable -> MediatorResult.Error(throwable) }
        }

    /**
     * Get the last page that was retrieved, that contained items.
     * From that last page, get the last item
     */
    private fun getRemoteKeyForLastItem(state: PagingState<Int, ActivityFeedModel>): ActivityFeedRemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { feed ->
                // Get the remote keys of the last item retrieved
                database.feedsRemoteKeysDao().remoteKeysFeedId(feed.id)
            }

    /**
     * Get the first page that was retrieved, that contained items.
     * From that first page, get the first item
     */
    private fun getRemoteKeyForFirstItem(state: PagingState<Int, ActivityFeedModel>): ActivityFeedRemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { feed ->
                // Get the remote keys of the first items retrieved
                database.feedsRemoteKeysDao().remoteKeysFeedId(feed.id)
            }

    /**
     * The paging library is trying to load data after the anchor position
     * Get the item closest to the anchor position
     */
    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ActivityFeedModel>): ActivityFeedRemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { feedId ->
                database.feedsRemoteKeysDao().remoteKeysFeedId(feedId)
            }
        }

    private fun getUser(userId: Long): Single<UserModel> = service.getUser(userId)
        .subscribeOn(Schedulers.io())

    /**
     * By default Paging library stops loading when retrieving an empty result, so we need to manually force our stop condition.
     * Here, we continue to retrieve elements till we reach our stop condition: we reach the oldest date.
     *
     * @param position The current starting date we are going to retrieve our feeds
     */
    private fun getActivityListModel(position: Date): Single<Pair<Date, ActivityListModel>> =
        service.getActivityFeeds(from = position.plusTwoWeeks().toApiFormat(), to = position.toApiFormat())
            .subscribeOn(Schedulers.io())
            .flatMap { activityListModel ->
                val endOfPagination: Boolean = position.time <= activityListModel.oldest.time
                if (activityListModel.activities.isEmpty() && !endOfPagination) {
                    getActivityListModel(position.plusTwoWeeks())
                } else Single.just(Pair(position, activityListModel))
            }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertToDb(
        loadType: LoadType,
        position: Date,
        feeds: List<ActivityFeedModel>,
        endOfPagination: Boolean
    ) = database.runInTransaction {
        // clear all tables in the database
        if (loadType == LoadType.REFRESH) {
            database.feedsRemoteKeysDao().clearAll()
            database.feesDao().clearAll()
        }
        val prevKey = if (position.time == to.time) null else position.minusTwoWeeks()
        val nextKey = if (endOfPagination) null else position.plusTwoWeeks()
        // Would be nice, but I can't cause I need to use auto-generated ids from db
//            val keys = feeds.map {
//                ActivityFeedRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
//            }

        val keys: List<ActivityFeedRemoteKeys> = database.feesDao().insertAll(feeds).map { id ->
            ActivityFeedRemoteKeys(id = id, prevKey = prevKey, nextKey = nextKey)
        }
        if (keys.isEmpty().not()) {
            database.feedsRemoteKeysDao().insertAll(keys)
        }
    }

    fun getAllLocalData() = database.feesDao().getAll()
}