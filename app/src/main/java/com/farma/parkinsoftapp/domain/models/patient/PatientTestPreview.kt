package com.farma.parkinsoftapp.domain.models.patient

import java.time.LocalDate

data class PatientTestPreview(
    val id: Long,
    val testDate: LocalDate,
    val questionCount: Int,
    val testTime: Int,
    val testName: String,
    var isSuccessTest: Boolean,
    val testType: TestType
)

enum class TestType(val value: String) {
    TEST_STIMULATION_DIARY("test_stimulation_diary"),
    STATE_OF_HEALTH_DIARY("state_of_health_diary");

    companion object {
        fun fromString(value: String?): TestType? {
            return entries.find { it.value.equals(value, ignoreCase = true) }
        }
    }
}
