package com.danielefavaro.qapital.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.danielefavaro.qapital_database.AppDatabase
import com.danielefavaro.qapital_database.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()
}