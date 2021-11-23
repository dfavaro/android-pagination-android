package com.danielefavaro.qapital_database.model.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel

@Dao
interface FeedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<ActivityFeedModel>): List<Long>

    @Query(
        "SELECT * FROM feeds WHERE " +
            "id = :id"
    )
    fun getFeedById(id: Long): PagingSource<Int, ActivityFeedModel>

    @Query(
        "SELECT * FROM feeds"
    )
    fun getAll(): PagingSource<Int, ActivityFeedModel>

    @Query("DELETE FROM feeds")
    fun clearAll()
}