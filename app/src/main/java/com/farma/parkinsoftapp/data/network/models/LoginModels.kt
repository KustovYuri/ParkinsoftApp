package com.farma.parkinsoftapp.data.network.models

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    val phone: String = "",
    val email: String = "patient@test.ru",
    val password: String = "12345"
)
data class LoginResponse(
    @SerializedName("session_key")
    val sessionKey: String,
    @SerializedName("role_id")
    val roleId: Int,
)