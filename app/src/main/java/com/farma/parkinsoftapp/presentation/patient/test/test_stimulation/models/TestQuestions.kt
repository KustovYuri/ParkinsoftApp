package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models

sealed interface TestStimulationTestQuestion {

    data class SingleAnswer(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): TestStimulationTestQuestion

    data class HumanPoint(
        val question: String,
        val sliderIsEnabled: Boolean = false,
        val commentIsEnabled: Boolean = false,
        val sliderValue: Int? = null,
        val comment: String? = null
    ): TestStimulationTestQuestion

    data class Slider(
        val question: String,
        val sliderAnswers: List<Pair<String, Int>>,
        val commentIsEnabled: Boolean = true,
        val comment: String? = null
    ): TestStimulationTestQuestion

    data class YesNo(
        val question: String,
        val answers: List<Pair<String, String>>,
        val comment: String = ""
    ): TestStimulationTestQuestion

    data class DisplaySlider(
        val question: String,
        val sliderValue: Int = 0
    ): TestStimulationTestQuestion

    data class Comment(
        val question: String,
        val comment: String = ""
    ): TestStimulationTestQuestion
}