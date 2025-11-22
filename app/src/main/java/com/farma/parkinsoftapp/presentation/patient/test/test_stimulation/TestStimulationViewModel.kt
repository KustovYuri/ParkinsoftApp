package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation


import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.mappers.convertToNativeTestRequest
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.SliderAnswer
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.YesNoAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TestStimulationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType
    private val testPreviewId = route.testId

    val enabledNextButton = derivedStateOf {
        when(val question = _uiState.value.data[_currentQuestionIndex.intValue]) {
            is TestQuestion.Comment -> question.comment.isNotBlank()
            is TestQuestion.DisplaySlider -> true
            is TestQuestion.HumanPoint -> {
                if (question.humanIsEnabled) {
                    question.selectedPoints.isNotEmpty()
                } else {
                    true
                }
            }
            is TestQuestion.SingleAnswer -> question.selectedAnswer != null
            is TestQuestion.Slider -> true
            is TestQuestion.YesNo -> question.answers.all { it.answer != null }
            else -> true
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

    fun selectAnswerInSingleAnswer(selectedAnswer: Pair<String, Int>) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: Boolean) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.YesNo).copy(
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

    fun changeSliderValueInHumanPoint(value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.HumanPoint) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.HumanPoint).copy(sliderValue = value)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeSliderValueInSliderVariant(name: String, value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.Slider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.Slider).copy(
                                sliderAnswers = question.sliderAnswers.map { sliderAnswer ->
                                    if (sliderAnswer.question == name) {
                                        sliderAnswer.copy(value = value)
                                    } else {
                                        sliderAnswer
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
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.DisplaySlider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.DisplaySlider).copy(sliderValue = value)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeHumanPointsInHumanPointsVariant(value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.HumanPoint) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.HumanPoint)
                            .copy(
                                selectedPoints = if (value == 0) {
                                    listOf(0)
                                }
                                else {
                                    if (!question.selectedPoints.contains(value)) {
                                        if (question.selectedPoints != listOf(0)) {
                                            question.selectedPoints + value
                                        } else {
                                            listOf(value)
                                        }
                                    } else {
                                        question.selectedPoints - value
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
        val selectedAnswer = _uiState.value.data[_currentQuestionIndex.intValue]

        if (selectedAnswer is TestQuestion.HumanPoint ||
            selectedAnswer is TestQuestion.Slider ||
            selectedAnswer is TestQuestion.YesNo ||
            selectedAnswer is TestQuestion.Comment) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        when(question) {
                            is TestQuestion.HumanPoint -> { question.copy(comment = value) }
                            is TestQuestion.Slider -> { question.copy(comment = value) }
                            is TestQuestion.YesNo -> { question.copy(comment = value) }
                            is TestQuestion.Comment -> { question.copy(comment = value) }
                            else -> { question }
                        }
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun finishTest(navigation: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.sendNativeTest(_uiState.value.data.convertToNativeTestRequest(testPreviewId)).collect {
                when(it) {
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isSending = false,
                            error = it.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isSending = true,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            navigation()
                        }
                    }
                }
            }
        }
    }
}


private fun getMocTestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.SingleAnswer(
            testId = 1,
            question = "Какая программа была сегодня самой эффективной?",
            answers = listOf("Первая" to 1, "Вторая" to 2, "Третья" to 3, "Четвертая" to 4, "Пятая" to 5),
        ),
        TestQuestion.HumanPoint(
            testId = 2,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на области стимуляции:"
        ),
        TestQuestion.HumanPoint(
            testId = 3,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на области стимуляции:"
        ),
        TestQuestion.HumanPoint(
            testId = 4,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:"
        ),
        TestQuestion.HumanPoint(
            testId = 5,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:"
        ),
        TestQuestion.HumanPoint(
            testId = 6,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на область с самой сильной болью:"
        ),
        TestQuestion.HumanPoint(
            testId = 7,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на область с самой сильной болью:"
        ),
        TestQuestion.HumanPoint(
            testId = 8,
            type = HumanImageType.FRONT,
            question = "На сколько в процентах снизилась боль в самой активной области?",
            humanIsEnabled = false,
            sliderIsEnabled = true,
            commentIsEnabled = true,
            sliderValue = 0,
            comment = ""
        ),
        TestQuestion.Slider(
            testId = 9,
            question = "Отметьте уровни боли при определенных видах деятельности из списка. (по шкале от 0 до 10, где 10 - самая сильная боль).",
            sliderAnswers = listOf(
                SliderAnswer(
                    questionId = 1,
                    question = "Сидя",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "Стоя",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "При ходьбе",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "Во время сна",
                ),
            ),
            commentIsEnabled = true
        ),
        TestQuestion.YesNo(
            testId = 10,
            question = "Оцените, было улучшение во времся следующих ситуаций?",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Смогли ли Вы дольше сидеть?",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 2,
                    question = "Смогли ли Вы дольше идти или стоять?",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 3,
                    question = "Было улучшение при рутиных видах деятельности (например при готовке еды, во время работы или уборки по дому)?",
                    yesScore = 1,
                    noScore = 0,
                )
            ),
        ),
        TestQuestion.DisplaySlider(
            testId = 11,
            question = "Сколько полос вы видите на дисплее во время наибольшего облегчения боли?"
        ),
        TestQuestion.SingleAnswer(
            testId = 12,
            question = "Оцените ощущения от стимуляции:",
            answers = listOf("Приятные" to 1, "Комфортные" to 2, "Некомфортные" to 3, "Болезненные" to 4),
        ),
        TestQuestion.SingleAnswer(
            testId = 13,
            question = "Оцените общую эффективность программы:",
            answers = listOf("Превосходно" to 1, "Хорошо" to 2, "Удовлетворительно" to 3, "Неэффективно" to 4),
        ),
        TestQuestion.Comment(
            testId = 14,
            question = "Определите изменеия вашего физического и эмоционального состояния, которые заметны Вам и вашему окружению"
        )
    )
}