package com.farma.parkinsoftapp.data.network.models

import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
import kotlinx.serialization.Serializable

@Serializable
data class PainDetectedRequest(
    val testPreviewId: Long,
    val singleAnswers: List<PainDetectedTestQuestions.SingleAnswer>,
    val humanPoints: List<PainDetectedTestQuestions.HumanPoint>,
    val sliderAnswers: List<PainDetectedTestQuestions.Slider>,
    val graphicAnswers: List<PainDetectedTestQuestions.Graphic>
) {
    companion object {
        fun getEmpty(testPreviewId: Long): PainDetectedRequest {
            return PainDetectedRequest(
                testPreviewId,
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
            )
        }
    }
}