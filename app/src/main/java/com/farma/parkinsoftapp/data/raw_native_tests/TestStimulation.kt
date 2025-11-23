package com.farma.parkinsoftapp.data.raw_native_tests

import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.models_common.SliderAnswer
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.models_common.YesNoAnswer

fun getTestStimulationTestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.SingleAnswer(
            testId = 1,
            question = "Какая программа была сегодня самой эффективной?",
            answers = listOf("Первая" to 1, "Вторая" to 2, "Третья" to 3, "Четвертая" to 4, "Пятая" to 5),
            maxScore = 5
        ),
        TestQuestion.HumanPoint(
            testId = 2,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на области стимуляции:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 3,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на области стимуляции:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 4,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 5,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на области, где стимуляция не перекрывала боль:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 6,
            type = HumanImageType.FRONT,
            question = "Нажмите на рисунке на область с самой сильной болью:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 7,
            type = HumanImageType.BACK,
            question = "Нажмите на рисунке на область с самой сильной болью:",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 8,
            type = HumanImageType.FRONT,
            question = "На сколько в процентах снизилась боль в самой активной области?",
            humanIsEnabled = false,
            sliderIsEnabled = true,
            commentIsEnabled = true,
            sliderValue = 0,
            comment = "",
            maxScore = 0
        ),
        TestQuestion.Slider(
            testId = 9,
            question = "Отметьте уровни боли при определенных видах деятельности из списка. (по шкале от 0 до 10, где 10 - самая сильная боль).",
            sliderAnswers = listOf(
                SliderAnswer(
                    questionId = 1,
                    question = "Сидя",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "Стоя",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "При ходьбе",
                ),
                SliderAnswer(
                    questionId = 1,
                    question = "Во время сна",
                ),
            ),
            commentIsEnabled = true,
            maxScore = 0
        ),
        TestQuestion.YesNo(
            testId = 10,
            question = "Оцените, было улучшение во время следующих ситуаций?",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Смогли ли Вы дольше сидеть?",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 2,
                    question = "Смогли ли Вы дольше идти или стоять?",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 3,
                    question = "Было улучшение при рутинных видах деятельности (например при готовке еды, во время работы или уборки по дому)?",
                    yesScore = 1,
                    noScore = 0,
                )
            ),
            maxScore = 3
        ),
        TestQuestion.DisplaySlider(
            testId = 11,
            question = "Сколько полос вы видите на дисплее во время наибольшего облегчения боли?",
            maxScore = 0
        ),
        TestQuestion.SingleAnswer(
            testId = 12,
            question = "Оцените ощущения от стимуляции:",
            answers = listOf("Приятные" to 1, "Комфортные" to 2, "Некомфортные" to 3, "Болезненные" to 4),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 13,
            question = "Оцените общую эффективность программы:",
            answers = listOf("Превосходно" to 1, "Хорошо" to 2, "Удовлетворительно" to 3, "Неэффективно" to 4),
            maxScore = 4
        ),
        TestQuestion.Comment(
            testId = 14,
            question = "Определите изменения вашего физического и эмоционального состояния, которые заметны Вам и вашему окружению"
        )
    )
}