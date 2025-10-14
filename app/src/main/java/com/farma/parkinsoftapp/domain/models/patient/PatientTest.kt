package com.farma.parkinsoftapp.domain.models.patient

data class PatientTest(
    val currentQuestionIndex: Int,
    val totalQuestions: Int,
    val question: Question,
    val selectedAnswer: String?,
    val isLastQuestion: Boolean,
    val allQuestion: List<Question>
)

data class Question(
    val id: Int,
    val text: String,
    val answers: List<String>
)
