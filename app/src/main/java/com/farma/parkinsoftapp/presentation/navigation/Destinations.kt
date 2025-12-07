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
    val userId: Long,
    val role: UserRole
)

//Пациент
@Serializable
object PatientAllTestsRoute

@Serializable
data class PatientTestRoute(
    val testId: Long,
    val testType: TestType
)

//Врач
@Serializable
object AllPatientsRoute

@Serializable
data class NewPatientRoute(
    val doctorId: Long
)

@Serializable
data class NewPatientTestsRoute(
    val id: Int,
    val doctorId: Long,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val age: Int,
    val phoneNumber: String,
    val birthdayDate: String,
    val diagnosis: String,
    val onTreatment: Boolean,
    val unreadTests: Int,
    val sex: Boolean
)

@Serializable
data class PatientInfoRoute(
    val patientId: Long
)

@Serializable
data class PatientCurrentTestRoute(
    val initials: String,
    val testDate: String,
    val testType: TestType,
    val testPreviewId: Long,
    val isNativeTest: Boolean,
    val maxScore: Int,
    val currentScore: Int,
    val pf: Float?,
    val rp: Float?,
    val bp: Float?,
    val gh: Float?,
    val vt: Float?,
    val sf: Float?,
    val re: Float?,
    val mh: Float?,
)