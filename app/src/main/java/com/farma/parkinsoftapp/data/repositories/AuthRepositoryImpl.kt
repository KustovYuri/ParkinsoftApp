package com.farma.parkinsoftapp.data.repositories

import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(): AuthRepository {
    val doctorPhoneNumber = listOf(
        "89024887366",
        "+79024887366",
        "11111111111",
        "89024444444"
    )

    override fun login(phoneNumber: String): UserRole {
        return if (doctorPhoneNumber.contains(phoneNumber)) {
            UserRole.DOCTOR
        } else {
            UserRole.PATIENT
        }
    }

    override fun isLogged(): Boolean {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}