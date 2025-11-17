package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import kotlinx.serialization.Serializable

sealed interface PainDetectedTestQuestions {
    @Serializable
    data class SingleAnswer(
        val id: Long,
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): PainDetectedTestQuestions

    @Serializable
    data class HumanPoint(
        val id: Long,
        val type: HumanImageType,
        val question: String,
        val selectedPoints: List<Int> = emptyList(),
    ): PainDetectedTestQuestions

    @Serializable
    data class Slider(
        val id: Long,
        val sliderAnswers: List<Pair<String, Int>>,
    ): PainDetectedTestQuestions

    @Serializable
    data class Graphic(
        val id: Long,
        val question: String,
        val graphicVariant: List<Pair<Int, String>>,
        val selectedVariant: String = ""
    ): PainDetectedTestQuestions
}