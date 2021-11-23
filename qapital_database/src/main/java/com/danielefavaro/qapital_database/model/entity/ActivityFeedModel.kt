package com.danielefavaro.qapital_database.model.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "feeds")
data class ActivityFeedModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "amount") val amount: Double?,
    @ColumnInfo(name = "timestamp") val timestamp: String,
    @ColumnInfo(name = "user_id") val userId: Long?,
    @ColumnInfo(name = "display_name") val displayName: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?
)