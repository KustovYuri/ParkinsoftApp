package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models

import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

data class PainDetectedState(
    val data: List<TestQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)