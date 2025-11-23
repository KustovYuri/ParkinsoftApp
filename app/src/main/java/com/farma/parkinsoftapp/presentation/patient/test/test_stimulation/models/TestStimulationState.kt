package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion


data class TestStimulationState(
    val data: List<TestQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)