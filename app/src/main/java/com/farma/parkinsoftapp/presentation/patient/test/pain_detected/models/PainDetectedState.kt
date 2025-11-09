package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models

data class PainDetectedState(
    val data: List<PainDetectedTestQuestions> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)