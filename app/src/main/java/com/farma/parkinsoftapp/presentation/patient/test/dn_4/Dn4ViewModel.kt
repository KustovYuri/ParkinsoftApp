package com.farma.parkinsoftapp.presentation.patient.test.dn_4

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.raw_native_tests.getDN4TestData
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.mappers.convertToNativeTestRequest
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationState
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class Dn4ViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType
    private val testPreviewId = route.testId

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    private val _uiState = mutableStateOf(
        TestStimulationState(
            data = getDN4TestData()
        )
    )
    val uiState: State<TestStimulationState> = _uiState

    val enabledNextButton = derivedStateOf {
        val question = _uiState.value.data[_currentQuestionIndex.intValue]
        if (question is TestQuestion.YesNo) {
            question.answers.all { it.answer != null }
        } else {
            true
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
}