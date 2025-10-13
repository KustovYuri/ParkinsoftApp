package com.farma.parkinsoftapp.presentation.navigation

import kotlinx.serialization.Serializable

//Логин
@Serializable
object LoginRoute

@Serializable
data class SmsRoute(val phoneNumber: String)

//Пациент
@Serializable
object PatientAllTestsRoute

@Serializable
object PatientTestRoute

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