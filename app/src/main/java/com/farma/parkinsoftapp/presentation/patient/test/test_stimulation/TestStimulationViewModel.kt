package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation


import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestStimulationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

    val enabledNextButton = derivedStateOf {
        when(val question = _uiState.value.data[_currentQuestionIndex.intValue]) {
            is TestStimulationTestQuestion.Comment -> question.comment.isNotBlank()
            is TestStimulationTestQuestion.DisplaySlider -> true
            is TestStimulationTestQuestion.HumanPoint -> !(question.commentIsEnabled && question.comment.isNullOrBlank())
            is TestStimulationTestQuestion.SingleAnswer -> question.selectedAnswer.isNotBlank()
            is TestStimulationTestQuestion.Slider -> true
            is TestStimulationTestQuestion.YesNo -> question.answers.all { it.second.isNotBlank() && question.comment.isNotBlank() }
        }
    }

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    private val _uiState = mutableStateOf(
        TestStimulationState(
            data = getMocTestData()
        )
    )
    val uiState: State<TestStimulationState> = _uiState

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

    fun selectAnswerInSingleAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
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

    fun changeSliderValueInHumanPoint(value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.HumanPoint) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.HumanPoint).copy(sliderValue = value)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeSliderValueInSliderVariant(name: String, value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.Slider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.Slider).copy(
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

    fun changeSliderValueInDisplaySliderVariant(value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.DisplaySlider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.DisplaySlider).copy(sliderValue = value)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeCommentValue(value: String) {
        val selectedAnswer = _uiState.value.data[_currentQuestionIndex.intValue]

        if (selectedAnswer is TestStimulationTestQuestion.HumanPoint ||
            selectedAnswer is TestStimulationTestQuestion.Slider ||
            selectedAnswer is TestStimulationTestQuestion.YesNo ||
            selectedAnswer is TestStimulationTestQuestion.Comment) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        when(question) {
                            is TestStimulationTestQuestion.HumanPoint -> { question.copy(comment = value) }
                            is TestStimulationTestQuestion.Slider -> { question.copy(comment = value) }
                            is TestStimulationTestQuestion.YesNo -> { question.copy(comment = value) }
                            is TestStimulationTestQuestion.Comment -> { question.copy(comment = value) }
                            else -> { question }
                        }
                    } else {
                        question
                    }
                }
            )
        }
    }
}


private fun getMocTestData(): List<TestStimulationTestQuestion> {
    return listOf(
        TestStimulationTestQuestion.SingleAnswer(
            question = "Какая программа была сегодня самой эффективной?",
            answers = listOf("Первая", "Вторая", "Третья", "Четвертая", "Пятая"),
        ),
        TestStimulationTestQuestion.HumanPoint(
            question = "Нажмите на рисунке на области стимуляции:"
        ),
        TestStimulationTestQuestion.HumanPoint(
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:"
        ),
        TestStimulationTestQuestion.HumanPoint(
            question = "Нажмите на рисунке на область с самой сильной болью:"
        ),
        TestStimulationTestQuestion.HumanPoint(
            question = "На сколько в процентах снизилась боль в самой активной области?",
            sliderIsEnabled = true,
            commentIsEnabled = true,
            sliderValue = 0,
            comment = ""
        ),
        TestStimulationTestQuestion.Slider(
            question = "Отметьте уровни боли при определенных видах деятельности из списка. (по шкале от 0 до 10, где 10 - самая сильная боль).",
            sliderAnswers = listOf("Сидя" to 0, "Стоя" to 0, "При ходьбе" to 0, "Во время сна" to 0),
            commentIsEnabled = true
        ),
        TestStimulationTestQuestion.YesNo(
            question = "Оцените, было улучшение во времся следующих ситуаций?",
            answers = listOf("Смогли ли Вы дольше сидеть?" to "", "Смогли ли Вы дольше идти или стоять?" to "", "Было улучшение при рутиных видах деятельности (например при готовке еды, во время работы или уборки по дому)?" to "",),
        ),
        TestStimulationTestQuestion.DisplaySlider(
            question = "Сколько полос вы видите на дисплее во время наибольшего облегчения боли?"
        ),
        TestStimulationTestQuestion.SingleAnswer(
            question = "Оцените ощущения от стимуляции:",
            answers = listOf("Приятные", "Комфортные", "Некомфортные", "Болезненные"),
        ),
        TestStimulationTestQuestion.SingleAnswer(
            question = "Оцените общую эффективность программы:",
            answers = listOf("Превосходно", "Хорошо", "Удовлетворительно", "Неэффективно"),
        ),
        TestStimulationTestQuestion.Comment(
            question = "Определите изменеия вашего физического и эмоционального состояния, которые заметны Вам и вашему окружению"
        )
    )
}