package com.farma.parkinsoftapp.presentation.navigation

import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.TestType
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
    val testId: Int,
    val testType: TestType
)

//Врач
@Serializable
object AllPatientsRoute

@Serializable
object NewPatientRoute

@Serializable
data class NewPatientTestsRoute(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val age: Int,
    val diagnosis: String,
    val onTreatment: Boolean,
    val unreadTests: Int,
    val sex: Boolean
)

@Serializable
data class PatientInfoRoute(
    val patientId: Int
)

@Serializable
object PatientCurrentTestRoute