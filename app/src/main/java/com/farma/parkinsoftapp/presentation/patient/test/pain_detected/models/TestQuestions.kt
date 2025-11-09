package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models

sealed interface PainDetectedTestQuestions {
    data class SingleAnswer(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): PainDetectedTestQuestions

    data class HumanPoint(
        val question: String,
        val sliderIsEnabled: Boolean = false,
        val commentIsEnabled: Boolean = false,
        val sliderValue: Int? = null,
        val comment: String? = null
    ): PainDetectedTestQuestions

    data class Slider(
        val sliderAnswers: List<Pair<String, Int>>,
    ): PainDetectedTestQuestions

    data class Graphic(
        val question: String,
        val sliderAnswers: List<Pair<String, Int>>,
        val commentIsEnabled: Boolean = true,
        val comment: String? = null
    ): PainDetectedTestQuestions
}