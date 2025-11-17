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
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedState
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
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
            is PainDetectedTestQuestions.Graphic -> question.selectedVariant.isNotBlank()
            is PainDetectedTestQuestions.HumanPoint -> question.selectedPoints.isNotEmpty()
            is PainDetectedTestQuestions.SingleAnswer -> question.selectedAnswer.isNotBlank()
            is PainDetectedTestQuestions.Slider -> true
        }
    }

    private val _uiState = mutableStateOf(
        PainDetectedState(
            data = getMockTestData()
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
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.Slider) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.Slider).copy(
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

    fun selectAnswerInSingleAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun selectAnswerInGraphicAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.Graphic) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.Graphic).copy(selectedVariant = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun changeHumanPointsInHumanPointsVariant(value: Int) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is PainDetectedTestQuestions.HumanPoint) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as PainDetectedTestQuestions.HumanPoint)
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

    fun finishTest(mainNavigation: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.finishPainDetectedTest(testPreviewId,_uiState.value.data).collect {
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

    private fun getMockTestData(): List<PainDetectedTestQuestions> {
        return listOf(
            PainDetectedTestQuestions.Slider(
                id = 1,
                sliderAnswers = listOf(
                    "Как бы Вы оценили интенсивность боли, которую испытываете сейчас, в настоящий момент?" to 0,
                    "Как бы вы оценили интенсивность наиболее сильного приступа боли за последние 4 нелели" to 0,
                    "В среднем, на сколько сильной была боль в течение последних 4 нелель" to 0
                )
            ),
            PainDetectedTestQuestions.Graphic(
                id = 2,
                question = "Выберете картинку, которая наиболее точно отражает характер протекания боли в вашем случае:",
                graphicVariant = listOf(
                    R.drawable.pain_variant_1 to "Непрерывная боль, немного меняющаяся по интенсивности",
                    R.drawable.pain_variant_2 to "Непрерывная боль с переодическими приступами",
                    R.drawable.pain_variant_3 to "Приступы боли без болевых ощущений в промежутках между ними",
                    R.drawable.pain_variant_4 to "Приступы боли, сопровождающиеся болевыми ощущениями в промежутках между ними"
                )
            ),
            PainDetectedTestQuestions.HumanPoint(
                id = 3,
                type = HumanImageType.HEAD,
                question = "Выберете те области, где вы испытываете наиболее сильную боль"
            ),
            PainDetectedTestQuestions.HumanPoint(
                id = 4,
                type = HumanImageType.BACK,
                question = "Выберете те области, где вы испытываете наиболее сильную боль"
            ),
            PainDetectedTestQuestions.HumanPoint(
                id = 5,
                type = HumanImageType.HEAD,
                question = "Выберете те области, в которые отдает боль"
            ),
            PainDetectedTestQuestions.HumanPoint(
                id = 6,
                type = HumanImageType.BACK,
                question = "Выберете те области, в которые отдает боль"
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 7,
                question = "Испытываете ли Вы ощущение жжения (например, как при ожоге крапивой) в области, которую отметили на рисунке?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 8,
                question = "Ощущете ли Вы покалывание или пощипывание в области боли (как покалывание от онимения или слабого электрического тока?)",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 9,
                question = "Возникает ли у Вас болезненные ощущения в указанной области при легком соприкосновении (с одеждой, одеялом)",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 10,
                question = "Возникают ли у Вас резкие приступы боли в указанной области, как удар током?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 11,
                question = "Возникают ли у Вас иногда болезненные ощущения в указанной области при воздействии холодного или горячего (например, воды, когда Вы моетесь)?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 12,
                question = "Ощущаете ли вы онемение в указанной области?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
            PainDetectedTestQuestions.SingleAnswer(
                id = 13,
                question = "Вызывает ли боль легкое нажатие на указанную область, например, нажатие пальцем?",
                answers = listOf(
                    "Совсем нет",
                    "Едва заметное",
                    "Незначительное",
                    "Умеренное",
                    "Сильное",
                    "Очень сильное",
                )
            ),
        )
    }
}