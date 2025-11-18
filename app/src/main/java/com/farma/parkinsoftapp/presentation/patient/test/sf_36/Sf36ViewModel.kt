package com.farma.parkinsoftapp.presentation.patient.test.sf_36

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.navigation.PatientTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.sf_36.models.Sf36State
import com.farma.parkinsoftapp.presentation.patient.test.sf_36.models.Sf36TestQuestions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Sf36ViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientTestRoute = savedStateHandle.toRoute()
    val testType = route.testType

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
        when(question) {
            is Sf36TestQuestions.SingleAnswer -> question.selectedAnswer.isNotBlank()
            is Sf36TestQuestions.YesNo -> question.answers.all { it.second.isNotBlank() }
            is Sf36TestQuestions.PreQuestion -> true
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

    fun selectAnswerInSingleAnswer(selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is Sf36TestQuestions.SingleAnswer) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as Sf36TestQuestions.SingleAnswer).copy(selectedAnswer = selectedAnswer)
                    } else {
                        question
                    }
                }
            )
        }
    }

    fun selectAnswerInYesNoAnswer(nameVariant: String, selectedAnswer: String) {
        if (_uiState.value.data[_currentQuestionIndex.intValue] is Sf36TestQuestions.YesNo) {
            _uiState.value = _uiState.value.copy(
                data = _uiState.value.data.mapIndexed { idx, question ->
                    if (idx == _currentQuestionIndex.intValue) {
                        (question as Sf36TestQuestions.YesNo).copy(
                            answers = question.answers.map { variant ->
                                if (variant.first == nameVariant) {
                                    variant.copy(second = selectedAnswer)
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

    fun getMockData(): List<Sf36TestQuestions> {
        return listOf(
            Sf36TestQuestions.SingleAnswer(
                question = "1. Как бы Вы в целом оценили состояние Вашего здоровья",
                answers = listOf("Отличное", "Очень хорошее", "Хорошее", "Посредственное", "Плохое")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "2. Как бы Вы в целом оценили свое здоровье сейчас по сравнению с тем, что было год назад",
                answers = listOf("Значительно лучше, чем год назад", "Несколько лучше, чем год назад", "Примерно так же, как год назад", "Несколько хуже, чем год назад", "Гораздо хуже, чем год назад")
            ),
            Sf36TestQuestions.PreQuestion(
                question = "3. Следующие вопросы касаются физических нагрузок, с которыми Вы, возможно, сталкиваетесь в течение своего обычного дня.\n\nОграничивает ли Вас состояние Вашего" +
                        "здоровья в настоящее время в выполнении перечисленных физических нагрузок? Если да, то в какой степени?"
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Тяжелые физические нагрузки, такие бег, поднятие тяжестей, занятие силовыми видами спорта.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Умеренные физические нагрузки, такие как передвинуть стол, поработать с пылесосом, собирать грибы или ягоды.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Поднять или нести сумку с продуктами.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Подняться пешком по лестнице на несколько пролетов.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Подняться пешком по лестнице на один пролет.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Наклониться, встать на колени, присесть на корточки.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Пройти расстояние более одного километра.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Пройти расстояние в несколько кварталов.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Пройти расстояние в один квартал.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Самостоятельно вымыться, одеться.",
                answers = listOf("Да, значительно ограничивает", "Да, немного ограничивает", "Хорошее", "Нет, совсем не ограничивает")
            ),
            Sf36TestQuestions.YesNo(
                question = "4. Бывало ли за последние 4 недели так, что Ваше физическое состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
                answers = listOf(
                    "Пришлось сократить количество времени, затрачиваемое на работу или другие дела" to "",
                    "Выполнили меньше, чем хотели" to "",
                    "Вы были ограничены в выполнении какого-либо определенного вида работ или другой деятельности" to "",
                    "Были трудности при выполнении своей работы или других дел они потребовали (например, дополнительных усилий)" to ""
                )
            ),
            Sf36TestQuestions.YesNo(
                question = "5. Бывало ли за последние 4 недели, что Ваше эмоциональное состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
                answers = listOf(
                    "Пришлось сократить количество времени, затрачиваемого на работу или другие дела." to "",
                    "Выполнили меньше, чем хотели" to "",
                    "Выполняли свою работу или другие дела не так аккуратно, как обычно" to "",
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "6. Насколько Ваше физическое и эмоциональное состояние в течение последних 4 недель мешало Вам проводить время с семьей, друзьями, соседями или в коллективе?",
                answers = listOf(
                    "Совсем не мешало",
                    "Немного",
                    "Умеренно",
                    "Сильно",
                    "Очень сильно"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "7. Насколько сильную физическую боль Вы испытывали за последние 4 недели?",
                answers = listOf(
                    "Совсем не испытывал(а)",
                    "Очень слабую",
                    "Слабую",
                    "Умеренную",
                    "Сильную",
                    "Очень сильную"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "8. В какой степени боль в течение последних 4 недель мешала Вам заниматься Вашей нормальной работой (включая работу вне дома или по дому)?",
                answers = listOf(
                    "Совсем не мешала",
                    "Немного",
                    "Умеренно",
                    "Сильно",
                    "Очень сильно"
                )
            ),
            Sf36TestQuestions.PreQuestion(
                question = "9. Следующие вопросы касаются того, как Вы себя чувствовали, и каким было Ваше настроение в течение последних 4 недель."
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя бодрым (ой)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы сильно нервничали?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя таким(ой) подавленным (ой), что ничто не могло Вас взбодрить?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя спокойным (ой) и умиротворенным (ой)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя полным (ой) сил и энергии?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя упавшим(ой) духом и печальным (ой)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя измученным (ой)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя счастливым (ой)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Вы чувствовали себя уставшим (ей)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Часто",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "10. Как часто за последние 4 недели Ваше физическое или эмоциональное состояние мешало Вам активно общаться с людьми (навещать друзей, родственников и т. п.)?",
                answers = listOf(
                    "Все время",
                    "Большую часть времени",
                    "Иногда",
                    "Редко",
                    "Ни разу"
                )
            ),
            Sf36TestQuestions.PreQuestion(
                question = "11. Насколько ВЕРНЫМ или НЕВЕРНЫМ представляются по отношению к Вам каждое из следующих утверждений?"
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Мне кажется, что я более склонен к болезням, чем другие",
                answers = listOf(
                    "Определенно верно",
                    "В основном верно",
                    "Не знаю",
                    "В основном неверно",
                    "Определенно неверно"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Мое здоровье не хуже, чем у большинства моих знакомых",
                answers = listOf(
                    "Определенно верно",
                    "В основном верно",
                    "Не знаю",
                    "В основном неверно",
                    "Определенно неверно"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "Я ожидаю, что мое здоровье ухудшится",
                answers = listOf(
                    "Определенно верно",
                    "В основном верно",
                    "Не знаю",
                    "В основном неверно",
                    "Определенно неверно"
                )
            ),
            Sf36TestQuestions.SingleAnswer(
                question = "У меня отличное здоровье",
                answers = listOf(
                    "Определенно верно",
                    "В основном верно",
                    "Не знаю",
                    "В основном неверно",
                    "Определенно неверно"
                )
            ),
        )
    }
}