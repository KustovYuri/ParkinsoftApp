package com.farma.parkinsoftapp.domain.models.patient

import kotlinx.serialization.Serializable

@Serializable
data class PatientTest(
    val currentQuestionIndex: Int,
    val totalQuestions: Int,
    val question: Question,
    val selectedAnswer: String?,
    val isLastQuestion: Boolean,
    val allQuestion: List<Question>
)

@Serializable
data class Question(
    val id: Int,
    val text: String,
    val answers: List<String>
)
