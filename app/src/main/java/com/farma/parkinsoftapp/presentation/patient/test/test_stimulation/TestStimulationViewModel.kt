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
import com.farma.parkinsoftapp.data.raw_native_tests.getTestStimulationTestData
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.mappers.convertToNativeTestRequest
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
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
            data = getTestStimulationTestData()
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
            mainRepository.sendNativeTest(_uiState.value.data.convertToNativeTestRequest(testPreviewId, testType)).collect {
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