package com.danielefavaro.qapital_network.model.service

import com.danielefavaro.qapital_network.model.entity.ActivityListModel
import com.danielefavaro.qapital_network.model.entity.UserModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QapitalApi {

    @GET("/activities")
    suspend fun getActivityFeeds(@Query("from") from: String, @Query("to") to: String): ActivityListModel

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Long): UserModel
}