package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType

sealed interface PainDetectedTestQuestions {
    data class SingleAnswer(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): PainDetectedTestQuestions

    data class HumanPoint(
        val type: HumanImageType,
        val question: String,
        val selectedPoints: List<Int> = emptyList(),
    ): PainDetectedTestQuestions

    data class Slider(
        val sliderAnswers: List<Pair<String, Int>>,
    ): PainDetectedTestQuestions

    data class Graphic(
        val question: String,
        val graphicVariant: List<Pair<Int, String>>,
        val selectedVariant: String = ""
    ): PainDetectedTestQuestions
}