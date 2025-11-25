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
    HADS1("hads1"),
    HADS2("hads2"),
    OSVESTRY("osvestry"),
    LANSS("lanss"),
    DN4("dn4"),
    SF36("sf36"),
    PAIN_DETECTED("pain_detected");

    companion object {
        fun fromString(value: String?): TestType? {
            return entries.find { it.value.equals(value, ignoreCase = true) }
        }
    }
}
