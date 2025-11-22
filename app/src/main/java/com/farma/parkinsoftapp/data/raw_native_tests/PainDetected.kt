package com.farma.parkinsoftapp.data.raw_native_tests

import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.SliderAnswer
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

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
                    question = "Как бы вы оценили интенсивность наиболее сильного приступа боли за последние 4 нелели",
                ),
                SliderAnswer(
                    questionId = 3,
                    question = "В среднем, на сколько сильной была боль в течение последних 4 нелель",
                ),
            )
        ),
        TestQuestion.Graphic(
            testId = 2,
            question = "Выберете картинку, которая наиболее точно отражает характер протекания боли в вашем случае:",
            graphicVariant = listOf(
                GraphicVariant(
                    image = R.drawable.thumbnail,
                    question = "Непрерывная боль, немного меняющаяся по интенсивности",
                    score = 1,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail_1,
                    question = "Непрерывная боль с переодическими приступами",
                    score = 2,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail_2,
                    question = "Приступы боли без болевых ощущений в промежутках между ними",
                    score = 3,
                ),
                GraphicVariant(
                    image = R.drawable.thumbnail_3,
                    question = "Приступы боли, сопровождающиеся болевыми ощущениями в промежутках между ними",
                    score = 4,
                ),
            ),
            score = 0
        ),
        TestQuestion.HumanPoint(
            testId = 3,
            type = HumanImageType.FRONT,
            question = "Выберете те области, где вы испытываете наиболее сильную боль"
        ),
        TestQuestion.HumanPoint(
            testId = 4,
            type = HumanImageType.BACK,
            question = "Выберете те области, где вы испытываете наиболее сильную боль"
        ),
        TestQuestion.HumanPoint(
            testId = 5,
            type = HumanImageType.FRONT,
            question = "Выберете те области, в которые отдает боль"
        ),
        TestQuestion.HumanPoint(
            testId = 6,
            type = HumanImageType.BACK,
            question = "Выберете те области, в которые отдает боль"
        ),
        TestQuestion.SingleAnswer(
            testId = 7,
            question = "Испытываете ли Вы ощущение жжения (например, как при ожоге крапивой) в области, которую отметили на рисунке?",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 8,
            question = "Ощущете ли Вы покалывание или пощипывание в области боли (как покалывание от онимения или слабого электрического тока?)",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 9,
            question = "Возникает ли у Вас болезненные ощущения в указанной области при легком соприкосновении (с одеждой, одеялом)",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 10,
            question = "Возникают ли у Вас резкие приступы боли в указанной области, как удар током?",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 11,
            question = "Возникают ли у Вас иногда болезненные ощущения в указанной области при воздействии холодного или горячего (например, воды, когда Вы моетесь)?",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 12,
            question = "Ощущаете ли вы онемение в указанной области?",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
        TestQuestion.SingleAnswer(
            testId = 13,
            question = "Вызывает ли боль легкое нажатие на указанную область, например, нажатие пальцем?",
            answers = listOf(
                "Совсем нет" to 1,
                "Едва заметное" to 2,
                "Незначительное" to 3,
                "Умеренное" to 4,
                "Сильное" to 5,
                "Очень сильное" to 6,
            )
        ),
    )
}