package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models

sealed interface TestQuestion {

    data class SingleAnswer(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): TestQuestion

    data class HumanPoint(
        val question: String,
        val sliderIsEnabled: Boolean = false,
        val commentIsEnabled: Boolean = false,
        val sliderValue: Int? = null,
        val comment: String? = null
    ): TestQuestion

    data class Slider(
        val question: String,
        val sliderAnswers: List<Pair<String, Int>>,
        val commentIsEnabled: Boolean = true,
        val comment: String? = null
    ): TestQuestion

    data class YesNo(
        val question: String,
        val answers: List<Pair<String, String>>,
        val comment: String = ""
    ): TestQuestion

    data class DisplaySlider(
        val question: String,
        val sliderValue: Int = 0
    ): TestQuestion

    data class Comment(
        val question: String,
        val comment: String = ""
    ): TestQuestion
}