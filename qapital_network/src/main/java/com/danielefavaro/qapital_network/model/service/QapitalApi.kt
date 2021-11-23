package com.danielefavaro.qapital_network.model.service

import com.danielefavaro.qapital_network.model.entity.ActivityListModel
import com.danielefavaro.qapital_network.model.entity.UserModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QapitalApi {

    @GET("/activities")
    fun getActivityFeeds(@Query("from") from: String, @Query("to") to: String): Single<ActivityListModel>

    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: Long): Single<UserModel>
}