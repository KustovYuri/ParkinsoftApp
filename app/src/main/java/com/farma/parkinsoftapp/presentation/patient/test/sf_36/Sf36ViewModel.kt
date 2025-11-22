package com.farma.parkinsoftapp.presentation.patient.test.sf_36

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
import com.farma.parkinsoftapp.presentation.patient.test.sf_36.models.Sf36State
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.YesNoAnswer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class Sf36ViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository,
) : ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType
    val testPreviewId = route.testId

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: IntState = _currentQuestionIndex

    private val _uiState = mutableStateOf(
        Sf36State(
            data = getMockData()
        )
    )
    val uiState: State<Sf36State> = _uiState

    val enabledNextButton = derivedStateOf {
        val question = _uiState.value.data[_currentQuestionIndex.intValue]
        when (question) {
            is TestStimulationTestQuestion.SingleAnswer -> question.selectedAnswer != null
            is TestStimulationTestQuestion.YesNo -> question.answers.all { it.answer != null }
            is TestStimulationTestQuestion.PreQuestion -> true
            is TestStimulationTestQuestion.Comment -> true
            is TestStimulationTestQuestion.DisplaySlider -> true
            is TestStimulationTestQuestion.HumanPoint -> true
            is TestStimulationTestQuestion.Slider -> true
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

    fun selectAnswerInSingleAnswer(selectedAnswer: Pair<String, Int>) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: Boolean) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is TestStimulationTestQuestion.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as TestStimulationTestQuestion.YesNo).copy(
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

    fun getMockData(): List<TestStimulationTestQuestion> {
        return listOf(
            TestStimulationTestQuestion.SingleAnswer(
                testId = 1,
                question = "1. Как бы Вы в целом оценили состояние Вашего здоровья",
                answers = listOf(
                    "Отличное" to 1,
                    "Очень хорошее" to 2,
                    "Хорошее" to 3,
                    "Посредственное" to 4,
                    "Плохое" to 5
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 2,
                question = "2. Как бы Вы в целом оценили свое здоровье сейчас по сравнению с тем, что было год назад",
                answers = listOf(
                    "Значительно лучше, чем год назад" to 1,
                    "Несколько лучше, чем год назад" to 2,
                    "Примерно так же, как год назад" to 3,
                    "Несколько хуже, чем год назад" to 4,
                    "Гораздо хуже, чем год назад" to 5
                )
            ),
            TestStimulationTestQuestion.PreQuestion(
                question = "3. Следующие вопросы касаются физических нагрузок, с которыми Вы, возможно, сталкиваетесь в течение своего обычного дня.\n\nОграничивает ли Вас состояние Вашего" +
                        "здоровья в настоящее время в выполнении перечисленных физических нагрузок? Если да, то в какой степени?"
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 3,
                question = "Тяжелые физические нагрузки, такие бег, поднятие тяжестей, занятие силовыми видами спорта.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 4,
                question = "Умеренные физические нагрузки, такие как передвинуть стол, поработать с пылесосом, собирать грибы или ягоды.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 5,
                question = "Поднять или нести сумку с продуктами.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 6,
                question = "Подняться пешком по лестнице на несколько пролетов.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 7,
                question = "Подняться пешком по лестнице на один пролет.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 8,
                question = "Наклониться, встать на колени, присесть на корточки.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает"  to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 9,
                question = "Пройти расстояние более одного километра.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 10,
                question = "Пройти расстояние в несколько кварталов.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 11,
                question = "Пройти расстояние в один квартал.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает"  to 2,
                    "Хорошее"  to 3,
                    "Нет, совсем не ограничивает"  to 4
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 12,
                question = "Самостоятельно вымыться, одеться.",
                answers = listOf(
                    "Да, значительно ограничивает" to 1,
                    "Да, немного ограничивает" to 2,
                    "Хорошее" to 3,
                    "Нет, совсем не ограничивает" to 4
                )
            ),
            TestStimulationTestQuestion.YesNo(
                testId = 13,
                question = "4. Бывало ли за последние 4 недели так, что Ваше физическое состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Пришлось сократить количество времени, затрачиваемое на работу или другие дела",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 2,
                        question = "Выполнили меньше, чем хотели",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 3,
                        question = "Вы были ограничены в выполнении какого-либо определенного вида работ или другой деятельности",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 4,
                        question = "Были трудности при выполнении своей работы или других дел они потребовали (например, дополнительных усилий)",
                        yesScore = 1,
                        noScore = 0,
                    ),
                )
            ),
            TestStimulationTestQuestion.YesNo(
                testId = 14,
                question = "5. Бывало ли за последние 4 недели, что Ваше эмоциональное состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
                answers = listOf(
                    YesNoAnswer(
                        questionId = 1,
                        question = "Пришлось сократить количество времени, затрачиваемого на работу или другие дела.",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 2,
                        question = "Выполнили меньше, чем хотели",
                        yesScore = 1,
                        noScore = 0,
                    ),
                    YesNoAnswer(
                        questionId = 3,
                        question = "Выполняли свою работу или другие дела не так аккуратно, как обычно",
                        yesScore = 1,
                        noScore = 0,
                    ),
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 15,
                question = "6. Насколько Ваше физическое и эмоциональное состояние в течение последних 4 недель мешало Вам проводить время с семьей, друзьями, соседями или в коллективе?",
                answers = listOf(
                    "Совсем не мешало" to 1,
                    "Немного" to 2,
                    "Умеренно" to 3,
                    "Сильно" to 4,
                    "Очень сильно" to 5,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 16,
                question = "7. Насколько сильную физическую боль Вы испытывали за последние 4 недели?",
                answers = listOf(
                    "Совсем не испытывал(а)" to 1,
                    "Очень слабую" to 2,
                    "Слабую" to 3,
                    "Умеренную" to 4,
                    "Сильную" to 5,
                    "Очень сильную" to 6
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 17,
                question = "8. В какой степени боль в течение последних 4 недель мешала Вам заниматься Вашей нормальной работой (включая работу вне дома или по дому)?",
                answers = listOf(
                    "Совсем не мешала" to 1,
                    "Немного" to 2,
                    "Умеренно" to 3,
                    "Сильно" to 4,
                    "Очень сильно" to 5,
                )
            ),
            TestStimulationTestQuestion.PreQuestion(
                question = "9. Следующие вопросы касаются того, как Вы себя чувствовали, и каким было Ваше настроение в течение последних 4 недель."
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 18,
                question = "Вы чувствовали себя бодрым (ой)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 19,
                question = "Вы сильно нервничали?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 20,
                question = "Вы чувствовали себя таким(ой) подавленным (ой), что ничто не могло Вас взбодрить?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 21,
                question = "Вы чувствовали себя спокойным (ой) и умиротворенным (ой)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 22,
                question = "Вы чувствовали себя полным (ой) сил и энергии?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 23,
                question = "Вы чувствовали себя упавшим(ой) духом и печальным (ой)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 24,
                question = "Вы чувствовали себя измученным (ой)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 25,
                question = "Вы чувствовали себя счастливым (ой)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 26,
                question = "Вы чувствовали себя уставшим (ей)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Часто" to 3,
                    "Иногда" to 4,
                    "Редко" to 5,
                    "Ни разу" to 6,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 27,
                question = "10. Как часто за последние 4 недели Ваше физическое или эмоциональное состояние мешало Вам активно общаться с людьми (навещать друзей, родственников и т. п.)?",
                answers = listOf(
                    "Все время" to 1,
                    "Большую часть времени" to 2,
                    "Иногда" to 3,
                    "Редко" to 4,
                    "Ни разу" to 5,
                )
            ),
            TestStimulationTestQuestion.PreQuestion(
                question = "11. Насколько ВЕРНЫМ или НЕВЕРНЫМ представляются по отношению к Вам каждое из следующих утверждений?"
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 28,
                question = "Мне кажется, что я более склонен к болезням, чем другие",
                answers = listOf(
                    "Определенно верно" to 1,
                    "В основном верно" to 2,
                    "Не знаю" to 3,
                    "В основном неверно" to 4,
                    "Определенно неверно" to 5,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 29,
                question = "Мое здоровье не хуже, чем у большинства моих знакомых",
                answers = listOf(
                    "Определенно верно" to 1,
                    "В основном верно" to 2,
                    "Не знаю" to 3,
                    "В основном неверно" to 4,
                    "Определенно неверно" to 5,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 30,
                question = "Я ожидаю, что мое здоровье ухудшится",
                answers = listOf(
                    "Определенно верно" to 1,
                    "В основном верно" to 2,
                    "Не знаю" to 3,
                    "В основном неверно" to 4,
                    "Определенно неверно" to 5,
                )
            ),
            TestStimulationTestQuestion.SingleAnswer(
                testId = 31,
                question = "У меня отличное здоровье",
                answers = listOf(
                    "Определенно верно" to 1,
                    "В основном верно" to 2,
                    "Не знаю" to 3,
                    "В основном неверно" to 4,
                    "Определенно неверно" to 5,
                )
            ),
        )
    }
}