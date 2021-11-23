package com.danielefavaro.qapital_network.model.entity

import androidx.annotation.Keep

@Keep
data class UserModel(
    val userId: Long,
    val displayName: String,
    val avatarUrl: String?
)