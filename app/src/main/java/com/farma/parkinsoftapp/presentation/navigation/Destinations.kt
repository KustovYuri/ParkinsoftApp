package com.farma.parkinsoftapp.presentation.navigation

import com.farma.parkinsoftapp.domain.models.user.UserRole
import kotlinx.serialization.Serializable

//Логин
@Serializable
object LoginRoute

@Serializable
data class SmsRoute(
    val phoneNumber: String,
    val userRole: UserRole
)

//Пациент
@Serializable
object PatientAllTestsRoute

@Serializable
data class PatientTestRoute(
    val testId: Int
)

//Врач
@Serializable
object AllPatientsRoute

@Serializable
object NewPatientRoute

@Serializable
object NewPatientTestsRoute

@Serializable
object PatientInfoRoute

@Serializable
object PatientCurrentTestRoute