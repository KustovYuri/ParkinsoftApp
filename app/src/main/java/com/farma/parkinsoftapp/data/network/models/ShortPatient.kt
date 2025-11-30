package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ShortPatient(
    val id: Long,
    val dischargeDate: String?,
    val isDischarge: Boolean,
    val testPreview: List<TestPreview>
)

@Serializable
data class TestPreview(
    val id: Long? = null,
    val patientId: Long,
    val testType: String,
    val testDate: String,
    val testTime: Int,
    val questionsCount: Int,
    val isCompleted: Boolean,
    val isViewed: Boolean?,
)
