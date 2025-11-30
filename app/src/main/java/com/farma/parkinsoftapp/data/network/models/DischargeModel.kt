package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class DischargeModel(
    val patientID: Long,
    val dischargeDateTime: String?
)
