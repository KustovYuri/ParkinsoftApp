package com.farma.parkinsoftapp.data.raw_native_tests

import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.models_common.YesNoAnswer

fun getSF36TestData(): List<TestQuestion> {
    return listOf(
        TestQuestion.SingleAnswer(
            testId = 1,
            question = "1. Как бы Вы в целом оценили состояние Вашего здоровья",
            answers = listOf(
                "Отличное" to 1,
                "Очень хорошее" to 2,
                "Хорошее" to 3,
                "Посредственное" to 4,
                "Плохое" to 5
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 2,
            question = "2. Как бы Вы в целом оценили свое здоровье сейчас по сравнению с тем, что было год назад",
            answers = listOf(
                "Значительно лучше, чем год назад" to 1,
                "Несколько лучше, чем год назад" to 2,
                "Примерно так же, как год назад" to 3,
                "Несколько хуже, чем год назад" to 4,
                "Гораздо хуже, чем год назад" to 5
            ),
            maxScore = 5
        ),
        TestQuestion.PreQuestion(
            question = "3. Следующие вопросы касаются физических нагрузок, с которыми Вы, возможно, сталкиваетесь в течение своего обычного дня.\n\nОграничивает ли Вас состояние Вашего" +
                    "здоровья в настоящее время в выполнении перечисленных физических нагрузок? Если да, то в какой степени?"
        ),
        TestQuestion.SingleAnswer(
            testId = 3,
            question = "Тяжелые физические нагрузки, такие бег, поднятие тяжестей, занятие силовыми видами спорта.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 4,
            question = "Умеренные физические нагрузки, такие как передвинуть стол, поработать с пылесосом, собирать грибы или ягоды.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 5,
            question = "Поднять или нести сумку с продуктами.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 6,
            question = "Подняться пешком по лестнице на несколько пролетов.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 7,
            question = "Подняться пешком по лестнице на один пролет.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 8,
            question = "Наклониться, встать на колени, присесть на корточки.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 9,
            question = "Пройти расстояние более одного километра.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 10,
            question = "Пройти расстояние в несколько кварталов.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 11,
            question = "Пройти расстояние в один квартал.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.SingleAnswer(
            testId = 12,
            question = "Самостоятельно вымыться, одеться.",
            answers = listOf(
                "Да, значительно ограничивает" to 1,
                "Да, немного ограничивает" to 2,
                "Нет, совсем не ограничивает" to 3
            ),
            maxScore = 4
        ),
        TestQuestion.YesNo(
            testId = 13,
            question = "4. Бывало ли за последние 2 недели так, что Ваше физическое состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
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
            ),
            maxScore = 4
        ),
        TestQuestion.YesNo(
            testId = 14,
            question = "5. Бывало ли за последние 2 недели, что Ваше эмоциональное состояние вызывало затруднения в Вашей работе или другой обычной повседневной деятельности, вследствие чего:",
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
            ),
            maxScore = 3
        ),
        TestQuestion.SingleAnswer(
            testId = 15,
            question = "6. Насколько Ваше физическое и эмоциональное состояние в течение последних 4 недель мешало Вам проводить время с семьей, друзьями, соседями или в коллективе?",
            answers = listOf(
                "Совсем не мешало" to 1,
                "Немного" to 2,
                "Умеренно" to 3,
                "Сильно" to 4,
                "Очень сильно" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 16,
            question = "7. Насколько сильную физическую боль Вы испытывали за последние 2 недели?",
            answers = listOf(
                "Совсем не испытывал(а)" to 1,
                "Очень слабую" to 2,
                "Слабую" to 3,
                "Умеренную" to 4,
                "Сильную" to 5,
                "Очень сильную" to 6
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 17,
            question = "8. В какой степени боль в течение последних 4 недель мешала Вам заниматься Вашей нормальной работой (включая работу вне дома или по дому)?",
            answers = listOf(
                "Совсем не мешала" to 1,
                "Немного" to 2,
                "Умеренно" to 3,
                "Сильно" to 4,
                "Очень сильно" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.PreQuestion(
            question = "9. Следующие вопросы касаются того, как Вы себя чувствовали, и каким было Ваше настроение в течение последних 4 недель."
        ),
        TestQuestion.SingleAnswer(
            testId = 18,
            question = "Вы чувствовали себя бодрым(ой)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 19,
            question = "Вы сильно нервничали?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 20,
            question = "Вы чувствовали себя таким(ой) подавленным(ой), что ничто не могло Вас взбодрить?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 21,
            question = "Вы чувствовали себя спокойным(ой) и умиротворенным(ой)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 22,
            question = "Вы чувствовали себя полным(ой) сил и энергии?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 23,
            question = "Вы чувствовали себя упавшим(ой) духом и печальным(ой)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 24,
            question = "Вы чувствовали себя измученным(ой)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 25,
            question = "Вы чувствовали себя счастливым(ой)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 26,
            question = "Вы чувствовали себя уставшим(ей)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Часто" to 3,
                "Иногда" to 4,
                "Редко" to 5,
                "Ни разу" to 6,
            ),
            maxScore = 6
        ),
        TestQuestion.SingleAnswer(
            testId = 27,
            question = "10. Как часто за последние 2 недели Ваше физическое или эмоциональное состояние мешало Вам активно общаться с людьми (навещать друзей, родственников и т. п.)?",
            answers = listOf(
                "Все время" to 1,
                "Большую часть времени" to 2,
                "Иногда" to 3,
                "Редко" to 4,
                "Ни разу" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.PreQuestion(
            question = "11. Насколько ВЕРНЫМ или НЕВЕРНЫМ представляются по отношению к Вам каждое из следующих утверждений?"
        ),
        TestQuestion.SingleAnswer(
            testId = 28,
            question = "Мне кажется, что я более склонен к болезням, чем другие",
            answers = listOf(
                "Определенно верно" to 1,
                "В основном верно" to 2,
                "Не знаю" to 3,
                "В основном неверно" to 4,
                "Определенно неверно" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 29,
            question = "Мое здоровье не хуже, чем у большинства моих знакомых",
            answers = listOf(
                "Определенно верно" to 1,
                "В основном верно" to 2,
                "Не знаю" to 3,
                "В основном неверно" to 4,
                "Определенно неверно" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 30,
            question = "Я ожидаю, что мое здоровье ухудшится",
            answers = listOf(
                "Определенно верно" to 1,
                "В основном верно" to 2,
                "Не знаю" to 3,
                "В основном неверно" to 4,
                "Определенно неверно" to 5,
            ),
            maxScore = 5
        ),
        TestQuestion.SingleAnswer(
            testId = 31,
            question = "У меня отличное здоровье",
            answers = listOf(
                "Определенно верно" to 1,
                "В основном верно" to 2,
                "Не знаю" to 3,
                "В основном неверно" to 4,
                "Определенно неверно" to 5,
            ),
            maxScore = 5
        ),
    )
}