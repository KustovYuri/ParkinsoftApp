package com.farma.parkinsoftapp.presentation.patient.test.pain_detected

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedState
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PainDetectedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    private val _uiState = mutableStateOf(
        PainDetectedState(
            data = getMockTestData()
        )
    )
    val uiState: State<PainDetectedState> = _uiState

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

    fun changeSliderValueInSliderVariant(name: String, value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.Slider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.Slider).copy(
                            sliderAnswers = question.sliderAnswers.map { sliderPair ->
                                if (sliderPair.first == name) {
                                    sliderPair.copy(second = value)
                                } else {
                                    sliderPair
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

    fun selectAnswerInSingleAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    private fun getMockTestData(): List<PainDetectedTestQuestions> {
        return listOf(
            PainDetectedTestQuestions.Slider(
                sliderAnswers = listOf(
                    "Как бы Вы оценили интенсивность боли, которую испытываете сейчас, в настоящий момент?" to 0,
                    "Как бы вы оценили интенсивность наиболее сильного приступа боли за последние 4 нелели" to 0,
                    "В среднем, на сколько сильной была боль в течение последних 4 нелель" to 0
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Испытываете ли Вы ощущение жжения (например, как при ожоге крапивой) в области, которую отметили на рисунке?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Ощущете ли Вы покалывание или пощипывание в области боли (как покалывание от онимения или слабого электрического тока?)",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Возникает ли у Вас болезненные ощущения в указанной области при легком соприкосновении (с одеждой, одеялом)",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Возникают ли у Вас резкие приступы боли в указанной области, как удар током?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Возникают ли у Вас иногда болезненные ощущения в указанной области при воздействии холодного или горячего (например, воды, когда Вы моетесь)?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Ощущаете ли вы онемение в указанной области?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                question = "Вызывает ли боль легкое нажатие на указанную область, например, нажатие пальцем?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
        )
    }
}