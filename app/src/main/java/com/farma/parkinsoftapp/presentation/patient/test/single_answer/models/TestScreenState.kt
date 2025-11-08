package com.farma.parkinsoftapp.presentation.patient.test.single_answer.models

import com.farma.parkinsoftapp.data.network.models.TestModel

data class TestScreenState(
    val data: List<TestModel> = emptyList(),
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)
