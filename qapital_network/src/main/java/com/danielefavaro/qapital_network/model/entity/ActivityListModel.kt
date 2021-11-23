package com.danielefavaro.qapital_network.model.entity

import androidx.annotation.Keep
import java.util.Date

@Keep
data class ActivityListModel(
    val oldest: Date,
    val activities: List<ActivityFeedModel>
)