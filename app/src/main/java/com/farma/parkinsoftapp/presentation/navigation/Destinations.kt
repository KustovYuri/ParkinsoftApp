package com.farma.parkinsoftapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
data class SmsRoute(val phoneNumber: String)

@Serializable
object PatientAllTestsRoute

@Serializable
object PatientTestRoute