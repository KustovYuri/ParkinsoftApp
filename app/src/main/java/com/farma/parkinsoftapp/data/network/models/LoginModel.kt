package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginModel(
    val userId: Long,
    val role: String,
)
