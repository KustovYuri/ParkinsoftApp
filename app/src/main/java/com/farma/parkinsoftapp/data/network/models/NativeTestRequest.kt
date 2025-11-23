package com.farma.parkinsoftapp.data.network.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import kotlinx.serialization.Serializable

@Serializable
data class NativeTestRequest(
    val testPreviewId: Long,
    val summaryCount: Int,
    val maxPoints: Int,
    val singleAnswers: List<SingleAnswerRequest>? = emptyList(),
    val humanPoints: List<HumanPointsRequest>? = emptyList(),
    val sliderAnswers: List<SliderAnswerRequest>? = emptyList(),
    val graphicAnswers: List<GraphicAnswerRequest>? = emptyList(),
    val yesNoAnswers: List<YesNoAnswerRequest>? = emptyList(),
    val displaySliderAnswers: List<DisplaySliderAnswerRequest>? = emptyList(),
    val commentAnswers: List<CommentAnswerRequest>? = emptyList(),
)

@Serializable
data class SingleAnswerRequest(
    val testId: Long,
    val selectedAnswer: String,
    val score: Int
)

@Serializable
data class HumanPointsRequest(
    val testId: Long,
    val type: HumanImageType,
    val selectedPoints: List<Int>,
    val score: Int
)

@Serializable
data class SliderAnswerRequest(
    val testId: Long,
    val questionId: Long,
    val sliderValue: Int,
    val score: Int
)

@Serializable
data class GraphicAnswerRequest(
    val testId: Long,
    val selectedVariant: String = "",
    val score: Int
)

@Serializable
data class YesNoAnswerRequest(
    val testId: Long,
    val questionId: Long,
    val answer: Boolean,
    val score: Int
)

@Serializable
data class DisplaySliderAnswerRequest(
    val testId: Long,
    val sliderValue: Int = 0,
    val score: Int
)

@Serializable
data class CommentAnswerRequest(
    val testId: Long,
    val comment: String = ""
)
