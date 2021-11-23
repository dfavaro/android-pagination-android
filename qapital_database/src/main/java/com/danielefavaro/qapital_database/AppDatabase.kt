package com.danielefavaro.qapital_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danielefavaro.qapital_database.model.converters.Converters
import com.danielefavaro.qapital_database.model.dao.FeedsDao
import com.danielefavaro.qapital_database.model.dao.FeedsRemoteKeysDao
import com.danielefavaro.qapital_database.model.entity.ActivityFeedModel
import com.danielefavaro.qapital_database.model.entity.ActivityFeedRemoteKeys

@Database(
    entities = [
        ActivityFeedModel::class,
        ActivityFeedRemoteKeys::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun feesDao(): FeedsDao
    abstract fun feedsRemoteKeysDao(): FeedsRemoteKeysDao
}
