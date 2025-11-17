package com.farma.parkinsoftapp.presentation.patient.test.single_answer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.single_answer.models.TestScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientTestViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType
    private val testId = route.testId

    private val _selectedAnswers = MutableStateFlow(mapOf<TestModel, TestAnswer>())
    val selectedAnswers = _selectedAnswers.asStateFlow()
    private val _uiState: MutableStateFlow<TestScreenState> = MutableStateFlow(TestScreenState())
    val uiState: StateFlow<TestScreenState> = _uiState

    private val _currentTestQuestionId = MutableStateFlow(0)
    val currentTestQuestionId = _currentTestQuestionId.asStateFlow()

    init {
        viewModelScope.launch {
            mainRepository.getPatientSelectedTest(testId, testType).collect { test ->
                when(test) {
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = test.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            data = test.result.also { allTests ->
                                allTests.forEach { test ->
                                    test.answers.find { it.isSelected }?.let { selectedAnswer ->
                                        _selectedAnswers.value = _selectedAnswers.value + (test to selectedAnswer)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun finishTest(navigation: () -> Unit) {
        _uiState.value = _uiState.value.copy(isSending = true)
        viewModelScope.launch {
            mainRepository.finishSingleAnswersTest(
                testAnswers = _selectedAnswers.value.values.toList().map {
                    it.copy(
                        isSelected = true,
                    )
                }
            )
            navigation()
        }
    }

    fun selectAnswer(testQuestion: TestModel, selectedAnswer: TestAnswer) {
        _selectedAnswers.value = _selectedAnswers.value + (testQuestion to selectedAnswer)
    }

    fun nextQuestion() {
        _currentTestQuestionId.value++
    }

    fun previousQuestion() {
        _currentTestQuestionId.value--
    }
}
