package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType

sealed interface TestStimulationTestQuestion {

    data class SingleAnswer(
        val testId: Long,
        val question: String,
        val answers: List<Pair<String, Int>>,
        val selectedAnswer: Pair<String, Int>? = null,
    ): TestStimulationTestQuestion

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
    ): TestStimulationTestQuestion

    data class Slider(
        val testId: Long,
        val question: String,
        val sliderAnswers: List<SliderAnswer>,
        val commentIsEnabled: Boolean = true,
        val comment: String? = null,
    ): TestStimulationTestQuestion

    data class YesNo(
        val testId: Long,
        val question: String,
        val answers: List<YesNoAnswer>,
        val comment: String = ""
    ): TestStimulationTestQuestion

    data class DisplaySlider(
        val testId: Long,
        val question: String,
        val sliderValue: Int = 0,
        val score: Int = 0
    ): TestStimulationTestQuestion

    data class Comment(
        val testId: Long,
        val question: String,
        val comment: String = ""
    ): TestStimulationTestQuestion
}
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