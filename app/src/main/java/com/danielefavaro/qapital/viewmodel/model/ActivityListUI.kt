package com.danielefavaro.qapital.viewmodel.model

import java.util.Date

data class ActivityListUI(
    val oldest: Date,
    val activities: List<ActivityFeedUI>
)