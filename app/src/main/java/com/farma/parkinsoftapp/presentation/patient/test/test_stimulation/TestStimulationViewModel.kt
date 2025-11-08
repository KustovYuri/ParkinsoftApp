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

}


private fun getMocTestData(): List<TestQuestion> {
    return listOf(
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
        TestQuestion.HumanPoint(
            question = "На сколько в процентах снизилась боль в самой активной области?",
            sliderIsEnabled = true,
            commentIsEnabled = true,
            sliderValue = 0,
            comment = ""
        ),
        TestQuestion.Slider(
            question = "Отметьте уровни боли при определенных видах деятельности из списка.",
            sliderAnswers = listOf("Сидя", "Стоя", "При ходьбе", "Во время сна"),
            comment = true
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