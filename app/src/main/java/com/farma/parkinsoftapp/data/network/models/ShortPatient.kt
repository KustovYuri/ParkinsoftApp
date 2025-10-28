package com.farma.parkinsoftapp.data.network.models

data class ShortPatient(
    val patientId: Long,
    val testPreview: List<TestPreview>
)

data class TestPreview(
    val id: Long? = null,
    val patientId: Long,
    val testType: String,
    val testDate: String,
    val testTime: Int,
    val questionsCount: Int,
    val isCompleted: Boolean,
    val isViewed: Boolean,
)
