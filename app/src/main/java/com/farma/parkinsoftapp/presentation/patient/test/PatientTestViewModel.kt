package com.farma.parkinsoftapp.presentation.patient.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.domain.models.patient.PatientTest
import com.farma.parkinsoftapp.domain.models.patient.Question
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PatientTestViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

    private val selectedAnswers = mutableMapOf<Int, String?>()

    private val _uiState: MutableStateFlow<PatientTest> = MutableStateFlow(
        PatientTest(
            currentQuestionIndex = 0,
            totalQuestions = 15,
            question = Question(id = 1, text = "", answers = emptyList()),
            selectedAnswer = null,
            isLastQuestion = false,
            allQuestion = emptyList()
        )
    )
    val uiState: StateFlow<PatientTest> = _uiState

    init {
        _uiState.value = mainRepository.getPatientSelectedTest(testType)
    }

    fun finishTest(testId: Int) {
        mainRepository.finishTest(testId)
    }

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        selectedAnswers[state.currentQuestionIndex] = answer
        _uiState.update { state.copy(selectedAnswer = answer) }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state != null) {
            if (state.currentQuestionIndex < state.allQuestion.lastIndex) {
                val nextIndex = state.currentQuestionIndex + 1
                _uiState.value = state.copy(
                    currentQuestionIndex = nextIndex,
                    question = state.allQuestion[nextIndex],
                    selectedAnswer = selectedAnswers[nextIndex],
                    isLastQuestion = nextIndex == state.allQuestion.lastIndex
                )
            }
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state != null) {
            if (state.currentQuestionIndex > 0) {
                val prevIndex = state.currentQuestionIndex - 1
                _uiState.value = state.copy(
                    currentQuestionIndex = prevIndex,
                    question = state.allQuestion[prevIndex],
                    selectedAnswer = selectedAnswers[prevIndex],
                    isLastQuestion = false
                )
            }
        }
    }
}
