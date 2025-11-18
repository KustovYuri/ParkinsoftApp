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
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.YesNoAnswer
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
            question.answers.all { it.answer != null }
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

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: Boolean) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.YesNo).copy(
                            answers = question.answers.map { variant ->
                                if (variant.question == nameVariant) {
                                    variant.copy(
                                        answer = selectedAnswer,
                                        score = if (selectedAnswer) {
                                            variant.yesScore
                                        } else {
                                            variant.noScore
                                        }
                                    )
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
                testId = 1,
                question = "Соответсвует ли боль, которую испытывает пациент, одному или нескольким из следующих определений?",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Ощущение жжения",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 2,
                        question = "Болезненное ощущение холода",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 3,
                        question = "Ощущение как от удара током",
                        yesScore = 1,
                        noScore = 0,
                    )
                ),
            ),
            TestStimulationTestQuestion.YesNo(
                testId = 2,
                question = "Сопровождается ли боль одним или несколькими из следующих симптомов в области ее локализации?",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Пощипыванием, ощущением ползания мурашек",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 2,
                        question = "Онемением",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 3,
                        question = "Зудом",
                        yesScore = 1,
                        noScore = 0,
                    )
                ),
            ),
            TestStimulationTestQuestion.YesNo(
                testId = 3,
                question = "Локализована ли боль в той же области, где осмотр выявляет один или оба следующих симптома:",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Пониженная чувствительность прикосновению",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 2,
                        question = "Пониженная чувствительность покалыванию",
                        yesScore = 1,
                        noScore = 0,
                    ),
                ),
            ),
            TestStimulationTestQuestion.YesNo(
                testId = 4,
                question = "Можно ли вызвать или усилить боль в области ее локализации:",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Проведя в этой области кисточкой",
                        yesScore = 1,
                        noScore = 0,
                    ),
                ),
            ),
        )
    }
}