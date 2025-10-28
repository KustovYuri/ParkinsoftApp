package com.farma.parkinsoftapp.data.network.models

data class TestModel(
    val previewId: Long,
    val questionName: String,
    val answers: List<TestAnswer>
)

data class TestAnswer(
    val previewId: Long,
    val testAnswerId: Long? = null,
    val testAnswer: String,
    val isSelected: Boolean,
    val answerPoint: Int,
)