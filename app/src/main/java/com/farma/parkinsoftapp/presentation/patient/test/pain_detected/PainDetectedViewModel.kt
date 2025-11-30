package com.farma.parkinsoftapp.presentation.patient.test.pain_detected

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.raw_native_tests.getPainDetectedTestData
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.mappers.convertToNativeTestRequest
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedState
import com.farma.parkinsoftapp.presentation.patient.test.models_common.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PainDetectedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository
) : ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    private val testPreviewId = route.testId
    val testType = route.testType

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    val nextButtonIsActive = derivedStateOf {
        val question = _uiState.value.data[_currentQuestionIndex.intValue]
        when(question) {
            is TestQuestion.Graphic -> question.selectedVariant != null
            is TestQuestion.HumanPoint -> question.selectedPoints.isNotEmpty()
            is TestQuestion.SingleAnswer -> question.selectedAnswer != null
            is TestQuestion.Slider -> true
            else -> true
        }
    }

    private val _uiState = mutableStateOf(
        PainDetectedState(
            data = getPainDetectedTestData()
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

    fun selectAnswerInSingleAnswer(selectedAnswer:  Pair<String, Int>) {
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

    fun selectAnswerInGraphicAnswer(selectedAnswer: GraphicVariant) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestQuestion.Graphic) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestQuestion.Graphic).copy(selectedVariant = selectedAnswer)
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
                                },
                                score = if (question.question == "Выберите те области, в которые отдает боль") {
                                    val previousHumanPoint = _uiState.value.data[_currentQuestionIndex.intValue - 1] as? TestQuestion.HumanPoint
                                    if (previousHumanPoint is TestQuestion.HumanPoint && previousHumanPoint.question == "Выберите те области, в которые отдает боль" && previousHumanPoint.score == 0 && value != 0) {
                                        2
                                    } else {
                                        if (value != 0) {
                                            2
                                        } else {
                                            0
                                        }
                                    }
                                } else {
                                    0
                                }
                            )
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun finishTest(mainNavigation: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.sendNativeTest(_uiState.value.data.convertToNativeTestRequest(testPreviewId)).collect {
                when(it) {
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = it.message,
                            isSending = false
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isSending = true
                        )
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            mainNavigation()
                        }
                    }
                }
            }
        }
    }
}