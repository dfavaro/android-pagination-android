package com.danielefavaro.qapital_network.model.entity

import androidx.annotation.Keep
import java.util.Date

@Keep
data class ActivityFeedModel(
    val message: String,
    val amount: Double,
    val userId: Long,
    val timestamp: Date
)