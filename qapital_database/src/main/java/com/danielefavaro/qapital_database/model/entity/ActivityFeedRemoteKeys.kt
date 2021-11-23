package com.danielefavaro.qapital_database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "feeds_remote_keys")
data class ActivityFeedRemoteKeys(
    @PrimaryKey val id: Long,
    val prevKey: Date?,
    val nextKey: Date?
)