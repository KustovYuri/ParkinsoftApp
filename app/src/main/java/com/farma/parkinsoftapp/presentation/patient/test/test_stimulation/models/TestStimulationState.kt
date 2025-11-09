package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models


data class TestStimulationState(
    val data: List<TestStimulationTestQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)