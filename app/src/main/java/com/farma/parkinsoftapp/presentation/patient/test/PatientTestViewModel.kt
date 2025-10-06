package com.farma.parkinsoftapp.presentation.patient.test

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class Question(
    val id: Int,
    val text: String,
    val answers: List<String>
)

sealed interface SurveyUiState {
    data class Content(
        val currentQuestionIndex: Int,
        val totalQuestions: Int,
        val question: Question,
        val selectedAnswer: String?,
        val isLastQuestion: Boolean
    ) : SurveyUiState
}

@HiltViewModel
class PatientTestViewModel @Inject constructor() : ViewModel() {

    private val questions = listOf(
        Question(
            id = 1,
            text = "Каким определениям соответствует боль, которую вы испытываете?",
            answers = listOf(
                "Ощущение жжения",
                "Болезненное ощущение холода",
                "Ощущение как от удара током",
                "Не соответствует ни одному"
            )
        ),
        Question(
            id = 2,
            text = "Насколько сильна боль, которую вы ощущаете сейчас?",
            answers = listOf("Слабая", "Средняя", "Сильная", "Очень сильная")
        ),
        Question(
            id = 3,
            text = "Как долго продолжается боль?",
            answers = listOf("Несколько секунд", "Минуты", "Часы", "Постоянно")
        ),
        Question(
            id = 4,
            text = "Насколько часто вы испытываете боль?",
            answers = listOf("Редко", "Иногда", "Часто", "Постоянно")
        )
    )

    // Храним ответы по индексу вопроса
    private val selectedAnswers = mutableMapOf<Int, String?>()

    private val _uiState = MutableStateFlow(
        SurveyUiState.Content(
            currentQuestionIndex = 0,
            totalQuestions = questions.size,
            question = questions[0],
            selectedAnswer = null,
            isLastQuestion = false
        )
    )
    val uiState: StateFlow<SurveyUiState> = _uiState

    fun selectAnswer(answer: String) {
        val state = _uiState.value
        selectedAnswers[state.currentQuestionIndex] = answer
        _uiState.update { state.copy(selectedAnswer = answer) }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < questions.lastIndex) {
            val nextIndex = state.currentQuestionIndex + 1
            _uiState.value = state.copy(
                currentQuestionIndex = nextIndex,
                question = questions[nextIndex],
                selectedAnswer = selectedAnswers[nextIndex],
                isLastQuestion = nextIndex == questions.lastIndex
            )
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex > 0) {
            val prevIndex = state.currentQuestionIndex - 1
            _uiState.value = state.copy(
                currentQuestionIndex = prevIndex,
                question = questions[prevIndex],
                selectedAnswer = selectedAnswers[prevIndex],
                isLastQuestion = false
            )
        }
    }

    fun getAllAnswers(): Map<Int, String?> = selectedAnswers.toMap()
}
