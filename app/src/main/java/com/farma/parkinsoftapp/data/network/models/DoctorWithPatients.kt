package com.farma.parkinsoftapp.data.network.models

import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientModel
import kotlinx.serialization.Serializable

@Serializable
data class DoctorWithPatientsModel(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val patients: List<PatientModel>
)