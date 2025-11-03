package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TestResultModel(
    val testQuestion: String,
    val testAnswer: String,
    val testScore: Int,
    val testMaxScope: Int
)
