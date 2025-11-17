package com.farma.parkinsoftapp.presentation.patient.test.sf_36.models

import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion

sealed interface Sf36TestQuestions {
    data class SingleAnswer(
        val question: String,
        val answers: List<String>,
        val selectedAnswer: String = ""
    ): Sf36TestQuestions

    data class YesNo(
        val question: String,
        val answers: List<Pair<String, String>>,
    ): Sf36TestQuestions

    data class PreQuestion(
        val question: String
    ): Sf36TestQuestions
}