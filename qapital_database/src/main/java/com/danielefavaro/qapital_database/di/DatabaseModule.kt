package com.danielefavaro.qapital_database.di

import android.content.Context
import androidx.room.Room
import com.danielefavaro.qapital_database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "qapital.db"
    ).fallbackToDestructiveMigration().build()
}