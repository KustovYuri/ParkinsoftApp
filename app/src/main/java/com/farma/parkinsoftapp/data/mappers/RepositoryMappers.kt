package com.farma.parkinsoftapp.data.mappers

import com.farma.parkinsoftapp.data.network.models.PainDetectedRequest
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
import kotlin.collections.forEach
import kotlin.collections.plus

fun List<PainDetectedTestQuestions>.convertToPainDetectedRequest(testPreviewId: Long): PainDetectedRequest {
    var painDetectedRequest = PainDetectedRequest.getEmpty(testPreviewId)

    this.forEach { question ->
        when(question) {
            is PainDetectedTestQuestions.Graphic -> {
                painDetectedRequest = painDetectedRequest.copy(
                    graphicAnswers = painDetectedRequest.graphicAnswers + question
                )
            }
            is PainDetectedTestQuestions.HumanPoint -> {
                painDetectedRequest = painDetectedRequest.copy(
                    humanPoints = painDetectedRequest.humanPoints + question
                )
            }
            is PainDetectedTestQuestions.SingleAnswer -> {
                painDetectedRequest = painDetectedRequest.copy(
                    singleAnswers = painDetectedRequest.singleAnswers + question
                )
            }
            is PainDetectedTestQuestions.Slider -> {
                painDetectedRequest = painDetectedRequest.copy(
                    sliderAnswers = painDetectedRequest.sliderAnswers + question
                )
            }
        }
    }

    return painDetectedRequest
}