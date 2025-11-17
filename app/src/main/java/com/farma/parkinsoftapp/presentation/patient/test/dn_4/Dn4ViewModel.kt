package com.farma.parkinsoftapp.presentation.patient.test.dn_4

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Dn4ViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    private val _uiState = mutableStateOf(
        TestStimulationState(
            data = getMockData()
        )
    )
    val uiState: State<TestStimulationState> = _uiState

    val enabledNextButton = derivedStateOf {
        val question = _uiState.value.data[_currentQuestionIndex.intValue]
        if (question is TestStimulationTestQuestion.YesNo) {
            question.answers.all { it.second.isNotBlank() }
        } else {
            false
        }
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.intValue < _uiState.value.data.size) {
            _currentQuestionIndex.intValue++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.intValue > 0) {
            _currentQuestionIndex.intValue--
        }
    }

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.YesNo).copy(
                            answers = question.answers.map { variant ->
                                if (variant.first == nameVariant) {
                                    variant.copy(second = selectedAnswer)
                                } else {
                                    variant
                                }
                            }
                        )
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun getMockData(): List<TestStimulationTestQuestion> {
        return listOf(
            TestStimulationTestQuestion.YesNo(
                question = "Соответсвует ли боль, которую испытывает пациент, одному или нескольким из следующих определений?",
                answers = listOf("Ощущение жжения" to "", "Болезненное ощущение холода" to "", "Ощущение как от удара током" to "",),
            ),
            TestStimulationTestQuestion.YesNo(
                question = "Сопровождается ли боль одним или несколькими из следующих симптомов в области ее локализации?",
                answers = listOf("Пощипыванием, ощущением ползания мурашек" to "", "Покалыванием" to "", "Онемением" to "", "Зудом" to ""),
            ),
            TestStimulationTestQuestion.YesNo(
                question = "Локализована ли боль в той же области, где осмотр выявляет один или оба следующих симптома:",
                answers = listOf("Пониженная чувствительность прикосновению" to "", "Пониженная чувствительность покалыванию" to ""),
            ),
            TestStimulationTestQuestion.YesNo(
                question = "Можно ли вызвать или усилить боль в области ее локализации:",
                answers = listOf("Проведя в этой области кисточкой" to ""),
            ),
        )
    }
}