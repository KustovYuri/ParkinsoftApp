package com.farma.parkinsoftapp.data.raw_native_tests

import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.models_common.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.models_common.SliderAnswer
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

fun getPainDetectedTestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.Slider(
            testId = 1,
            question = "",
            sliderAnswers = listOf(
                SliderAnswer(
                    questionId = 1,
                    question = "Как бы Вы оценили интенсивность боли, которую испытываете сейчас, в настоящий момент?",
                ),
                SliderAnswer(
                    questionId = 2,
                    question = "Как бы вы оценили интенсивность наиболее сильного приступа боли за последние 4 недели",
                ),
                SliderAnswer(
                    questionId = 3,
                    question = "В среднем, на сколько сильной была боль в течение последних 4 недель",
                ),
            ),
            maxScore = 0
        ),
        TestQuestion.Graphic(
            testId = 2,
            question = "Выберите картинку, которая наиболее точно отражает характер протекания боли в вашем случае:",
            graphicVariant = listOf(
                GraphicVariant(
                    image = R.drawable.thumbnail__1_,
                    question = "Непрерывная боль, немного меняющаяся по интенсивности",
                    score = 0,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail__2_,
                    question = "Непрерывная боль с переодическими приступами",
                    score = -1,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail__3_,
                    question = "Приступы боли без болевых ощущений в промежутках между ними",
                    score = 1,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail__4_,
                    question = "Приступы боли, сопровождающиеся болевыми ощущениями в промежутках между ними",
                    score = 1,
                ),
            ),
            score = 0,
            maxScore = 1
        ),
        TestQuestion.HumanPoint(
            testId = 3,
            type = HumanImageType.FRONT,
            question = "Выберите те области, где вы испытываете наиболее сильную боль",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 4,
            type = HumanImageType.BACK,
            question = "Выберите те области, где вы испытываете наиболее сильную боль",
            maxScore = 0
        ),
        TestQuestion.HumanPoint(
            testId = 5,
            type = HumanImageType.FRONT,
            question = "Выберите те области, в которые отдает боль",
            maxScore = 2
        ),
        TestQuestion.HumanPoint(
            testId = 6,
            type = HumanImageType.BACK,
            question = "Выберите те области, в которые отдает боль",
            maxScore = 0
        ),
        TestQuestion.SingleAnswer(
            testId = 7,
            question = "Испытываете ли Вы ощущение жжения (например, как при ожоге крапивой) в области, которую отметили на рисунке?",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 8,
            question = "Ощущаете ли Вы покалывание или пощипывание в области боли (как покалывание от онемения или слабого электрического тока?)",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 9,
            question = "Возникает ли у Вас болезненные ощущения в указанной области при легком соприкосновении (с одеждой, одеялом)",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 10,
            question = "Возникают ли у Вас резкие приступы боли в указанной области, как удар током?",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 11,
            question = "Возникают ли у Вас иногда болезненные ощущения в указанной области при воздействии холодного или горячего (например, воды, когда Вы моетесь)?",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 12,
            question = "Ощущаете ли вы онемение в указанной области?",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 13,
            question = "Вызывает ли боль легкое нажатие на указанную область, например, нажатие пальцем?",
            answers = listOf(
                "Совсем нет" to 0,
                "Едва заметное" to 1,
                "Незначительное" to 2,
                "Умеренное" to 3,
                "Сильное" to 4,
                "Очень сильное" to 5,
            ),
            maxScore = 5
        ),
    )
}