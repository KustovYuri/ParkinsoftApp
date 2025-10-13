package com.farma.parkinsoftapp.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

interface ApiService {

    @POST("/api/login/")
    suspend fun login(
        @Body body: LoginRequest = LoginRequest()
    ): Response<LoginResponse>

}