package com.farma.parkinsoftapp.data.repositories

import com.farma.parkinsoftapp.data.network.httpExceptionHandler
import com.farma.parkinsoftapp.data.network.ktor.KtorService
import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ktorService: KtorService
): AuthRepository {
    override fun login(phoneNumber: String): Flow<Result<LoginModel>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.login(phoneNumber)
        }
        emit(result)
    }
}