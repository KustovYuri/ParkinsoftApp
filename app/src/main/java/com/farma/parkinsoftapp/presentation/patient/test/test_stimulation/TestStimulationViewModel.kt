package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TestStimulationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _uiState = MutableStateFlow(
        TestStimulationState(
            data = getMocTestData()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _uiState.value.data.size) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }

    fun selectAnswerInSingleAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.value] is TestQuestion.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.value) {
                        (question as TestQuestion.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.value] is TestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.value) {
                        (question as TestQuestion.YesNo).copy(
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
        if (_uiState.value.data[_currentQuestionIndex.value] is TestQuestion.HumanPoint) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.value) {
                        (question as TestQuestion.HumanPoint).copy(sliderValue = value)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeSliderValueInSliderVariant(name: String, value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.value] is TestQuestion.Slider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.value) {
                        (question as TestQuestion.Slider).copy(
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

    fun changeCommentValue(value: String) {
        val selectedAnswer = _uiState.value.data[_currentQuestionIndex.value]

        if (selectedAnswer is TestQuestion.HumanPoint ||
            selectedAnswer is TestQuestion.Slider ||
            selectedAnswer is TestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.value) {
                        when(question) {
                            is TestQuestion.HumanPoint -> { question.copy(comment = value) }
                            is TestQuestion.Slider -> { question.copy(comment = value) }
                            is TestQuestion.YesNo -> { question.copy(comment = value) }
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


private fun getMocTestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.HumanPoint(
            question = "На сколько в процентах снизилась боль в самой активной области?",
            sliderIsEnabled = true,
            commentIsEnabled = true,
            sliderValue = 0,
            comment = ""
        ),
        TestQuestion.SingleAnswer(
            question = "Какая программа была сегодня самой эффективной?",
            answers = listOf("Первая", "Вторая", "Третья", "Четвертая", "Пятая"),
        ),
        TestQuestion.HumanPoint(
            question = "Нажмите на рисунке на области стимуляции:"
        ),
        TestQuestion.HumanPoint(
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:"
        ),
        TestQuestion.HumanPoint(
            question = "Нажмите на рисунке на область с самой сильной болью:"
        ),
        TestQuestion.Slider(
            question = "Отметьте уровни боли при определенных видах деятельности из списка. (по шкале от 0 до 10, где 10 - самая сильная боль).",
            sliderAnswers = listOf("Сидя" to 0, "Стоя" to 0, "При ходьбе" to 0, "Во время сна" to 0),
            commentIsEnabled = true
        ),
        TestQuestion.YesNo(
            question = "Оцените, было улучшение во времся следующих ситуаций?",
            answers = listOf("Смогли ли Вы дольше сидеть?" to "", "Смогли ли Вы дольше идти или стоять?" to "", "Было улучшение при рутиных видах деятельности (например при готовке еды, во время работы или уборки по дому)?" to "",),
        ),
        TestQuestion.DisplaySlider(
            question = "Сколько полос вы видите на дисплее во время наибольшего облегчения боли?"
        ),
        TestQuestion.SingleAnswer(
            question = "Оцените ощущения от стимуляции:",
            answers = listOf("Приятные", "Комфортные", "Некомфортные", "Болезненные"),
        ),
        TestQuestion.SingleAnswer(
            question = "Оцените общую эффективность программы:",
            answers = listOf("п=Превосходно", "Хорошо", "Удовлетворительно", "Неэффективно"),
        ),
        TestQuestion.Comment(
            question = "Определите изменеия вашего физического и эмоционального состояния, которые заметны Вам и вашему окружению"
        )
    )
}