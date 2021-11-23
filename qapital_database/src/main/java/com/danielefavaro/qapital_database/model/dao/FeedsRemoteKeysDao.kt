package com.danielefavaro.qapital_database.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danielefavaro.qapital_database.model.entity.ActivityFeedRemoteKeys

@Dao
interface FeedsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<ActivityFeedRemoteKeys>)

    @Query("SELECT * FROM feeds_remote_keys")
    fun getAll(): List<ActivityFeedRemoteKeys>

    @Query("SELECT * FROM feeds_remote_keys WHERE id = :feedId")
    fun remoteKeysFeedId(feedId: Long): ActivityFeedRemoteKeys?

    @Query(
        "SELECT * FROM feeds_remote_keys " +
            "ORDER BY id DESC " +
            "LIMIT 1"
    )
    fun getLast(): ActivityFeedRemoteKeys

    @Query("DELETE FROM feeds_remote_keys")
    fun clearAll()
}