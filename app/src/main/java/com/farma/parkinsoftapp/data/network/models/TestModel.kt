package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TestModel(
    val previewId: Long,
    val questionId: Long,
    val questionName: String,
    val answers: List<TestAnswer>
)

@Serializable
data class TestAnswer(
    val questionId: Long,
    val previewId: Long,
    val testAnswerId: Long? = null,
    val testAnswer: String,
    val isSelected: Boolean,
    val answerPoint: Int,
)