package com.farma.parkinsoftapp.presentation.patient.test.models_common

sealed class TestQuestion(val id: Long) {

    data class SingleAnswer(
        val testId: Long,
        val question: String,
        val answers: List<Pair<String, Int>>,
        val selectedAnswer: Pair<String, Int>? = null,
        val maxScore: Int
    ): TestQuestion(testId)

    data class HumanPoint(
        val testId: Long,
        val type: HumanImageType,
        val question: String,
        val humanIsEnabled: Boolean = true,
        val selectedPoints: List<Int> = emptyList(),
        val sliderIsEnabled: Boolean = false,
        val commentIsEnabled: Boolean = false,
        val sliderValue: Int? = null,
        val comment: String? = null,
        val score: Int = 0,
        val sliderScore: Int = 0,
        val maxScore: Int
    ): TestQuestion(testId)

    data class Slider(
        val testId: Long,
        val question: String,
        val sliderAnswers: List<SliderAnswer>,
        val commentIsEnabled: Boolean = true,
        val comment: String? = null,
        val maxScore: Int,
    ): TestQuestion(testId)

    data class YesNo(
        val testId: Long,
        val question: String,
        val answers: List<YesNoAnswer>,
        val maxScore: Int,
        val comment: String = ""
    ): TestQuestion(testId)

    data class DisplaySlider(
        val testId: Long,
        val question: String,
        val sliderValue: Int = 0,
        val score: Int = 0,
        val maxScore: Int,
    ): TestQuestion(testId)

    data class Comment(
        val testId: Long,
        val question: String,
        val comment: String = ""
    ): TestQuestion(testId)

    data class PreQuestion(
        val question: String
    ): TestQuestion(id = -1)

    data class Graphic(
        val testId: Long,
        val question: String,
        val graphicVariant: List<GraphicVariant>,
        val selectedVariant: GraphicVariant? = null,
        val score: Int,
        val maxScore: Int,
    ): TestQuestion(testId)

}

data class GraphicVariant(
    val image: Int,
    val question: String,
    val score: Int
)
data class YesNoAnswer(
    val questionId: Long,
    val question: String,
    val answer: Boolean? = null,
    val yesScore: Int,
    val noScore: Int,
    val score: Int = 0,
)
data class SliderAnswer(
    val questionId: Long,
    val question: String,
    val value: Int = 0,
    val score: Int = 0
)