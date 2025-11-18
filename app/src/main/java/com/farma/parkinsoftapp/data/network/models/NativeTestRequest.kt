package com.farma.parkinsoftapp.data.network.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion

data class NativeTestRequest(
    val testPreviewId: Long,
    val singleAnswers: List<SingleAnswerRequest>?,
    val humanPoints: List<HumanPointsRequest>?,
    val sliderAnswers: List<SliderAnswerRequest>?,
    val graphicAnswers: List<GraphicAnswerRequest>?,
    val yesNoAnswers: List<YesNoAnswerRequest>?,
    val displaySliderAnswers: List<DisplaySliderAnswerRequest?>,
    val commentAnswers: List<CommentAnswerRequest>?,
)

data class SingleAnswerRequest(
    val testId: Long,
    val selectedAnswer: String,
    val score: Int
)

data class HumanPointsRequest(
    val testId: Long,
    val type: HumanImageType,
    val selectedPoints: List<Int>,
    val score: Int
)

data class SliderAnswerRequest(
    val testId: Long,
    val questionId: Long,
    val sliderValue: Int,
    val score: Int
)

data class GraphicAnswerRequest(
    val testId: Long,
    val selectedVariant: String = "",
    val score: Int
)

data class YesNoAnswerRequest(
    val testId: Long,
    val questionId: Long,
    val answer: Boolean,
    val score: Int
)

data class DisplaySliderAnswerRequest(
    val testId: Long,
    val sliderValue: Int = 0,
    val score: Int
)

data class CommentAnswerRequest(
    val testId: Long,
    val comment: String = ""
)
