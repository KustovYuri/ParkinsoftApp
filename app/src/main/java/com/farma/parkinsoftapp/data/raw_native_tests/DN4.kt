package com.farma.parkinsoftapp.data.raw_native_tests

import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.models_common.YesNoAnswer

fun getDN4TestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.YesNo(
            testId = 1,
            question = "Соответствует ли боль, которую испытывает пациент, одному или нескольким из следующих определений?",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Ощущение жжения",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 2,
                    question = "Болезненное ощущение холода",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 3,
                    question = "Ощущение как от удара током",
                    yesScore = 1,
                    noScore = 0,
                )
            ),
            maxScore = 3
        ),
        TestQuestion.YesNo(
            testId = 2,
            question = "Сопровождается ли боль одним или несколькими из следующих симптомов в области ее локализации?",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Пощипыванием, ощущением ползания мурашек",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 2,
                    question = "Онемением",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 3,
                    question = "Зудом",
                    yesScore = 1,
                    noScore = 0,
                )
            ),
            maxScore = 3
        ),
        TestQuestion.PreQuestion("Следующая часть теста предназначена для врача, передайте пожалуйста ему телефон."),
        TestQuestion.YesNo(
            testId = 3,
            question = "Локализована ли боль в той же области, где осмотр выявляет один или оба следующих симптома:",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Пониженная чувствительность прикосновению",
                    yesScore = 1,
                    noScore = 0,
                ),
                YesNoAnswer(
                    questionId = 2,
                    question = "Пониженная чувствительность покалыванию",
                    yesScore = 1,
                    noScore = 0,
                ),
            ),
            maxScore = 2
        ),
        TestQuestion.YesNo(
            testId = 4,
            question = "Можно ли вызвать или усилить боль в области ее локализации:",
            answers = listOf(
                YesNoAnswer(
                    questionId = 1,
                    question = "Проведя в этой области кисточкой",
                    yesScore = 1,
                    noScore = 0,
                ),
            ),
            maxScore = 1
        ),
    )
}