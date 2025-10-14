package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.domain.models.user.UserRole

interface AuthRepository {
    fun login(phoneNumber: String): UserRole

    fun isLogged(): Boolean

    fun logout()
}