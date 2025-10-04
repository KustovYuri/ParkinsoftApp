package com.farma.parkinsoftapp.presentation.patient.all_tests.models

import java.time.LocalDate

data class TestPreviewModel(
    val id: Int,
    val title: String,
    val questionCount: Int,
    val durationMinutes: Int,
    val status: QuestionnaireStatus,
    val dateLabel: LocalDate
)

enum class QuestionnaireStatus {
    NEEDS_FILL, FILLED
}