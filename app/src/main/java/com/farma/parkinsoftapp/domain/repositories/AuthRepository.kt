package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.user.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(phoneNumber: String): Flow<Result<LoginModel>>
}