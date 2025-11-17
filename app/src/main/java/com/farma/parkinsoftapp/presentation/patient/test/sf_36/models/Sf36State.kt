package com.farma.parkinsoftapp.presentation.patient.test.sf_36.models

import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion

data class Sf36State(
    val data: List<Sf36TestQuestions> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)
