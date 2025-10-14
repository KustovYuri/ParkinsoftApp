package com.farma.parkinsoftapp.domain.models.patient

import java.time.LocalDate

data class PatientTestPreview(
    val id: Int,
    val testDate: LocalDate,
    val questionCount: Int,
    val testTime: Int,
    val testName: String,
    val isSuccessTest: Boolean,
    val testType: TestType
)

enum class TestType {
    TEST_SIMULATION, STATE_OF_HEALTH
}
