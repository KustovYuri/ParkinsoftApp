package com.farma.parkinsoftapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.farma.parkinsoftapp.data.local.data_store.SessionDataStore
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTest
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.Question
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore
): MainRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPatientTests(): Flow<List<PatientTestPreview>> = flow {
        emit(patientTestPreview)
    }

    override fun getPatientSelectedTest(testType: TestType): PatientTest {
        return when(testType) {
            TestType.TEST_SIMULATION -> PatientTest(
                currentQuestionIndex = 0,
                totalQuestions = patientTestsSimulationTests.size,
                question = patientTestsSimulationTests[0],
                selectedAnswer = null,
                isLastQuestion = false,
                allQuestion = patientTestsSimulationTests
            )
            TestType.STATE_OF_HEALTH -> PatientTest(
                currentQuestionIndex = 0,
                totalQuestions = patientStateOfHeathTests.size,
                question = patientStateOfHeathTests[0],
                selectedAnswer = null,
                isLastQuestion = false,
                allQuestion = patientStateOfHeathTests
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun finishTest(testId: Int) {
        patientTestPreview.find { it.id == testId }?.isSuccessTest = true
    }

    override fun getAllPatients(): Flow<List<Patient>> = doctorPatients

    override fun getPatient(patientId: Int): Patient {
        return doctorPatients.value.find { it.id == patientId } ?: doctorPatients.value[0]
    }

    override fun addNewPatient(patient: Patient): Int {
        val patientId = doctorPatients.value.size + 2

        doctorPatients.value = doctorPatients.value + patient.copy(id = patientId)
        return patientId
    }

    override fun getUserRole(): Flow<UserRoleValues> {
        return sessionDataStore.getCurrentUserRole().map { it ->
            UserRoleValues.fromValue(it) ?: UserRoleValues.UNAUTHORIZED
        }
    }

    override suspend fun setUserRole(newUserRole: UserRoleValues) {
        sessionDataStore.setCurrentUserRole(newUserRole)
    }
}

private val doctorPatients = MutableStateFlow(
    listOf(
        Patient(1, "Мария", "Жукова", "Дмитриевна", 52, "Заболевание", true, 10, false),
        Patient(2, "Михаил", "Миронов", "Андреевич", 33, "Заболевание", true, 7, true),
        Patient(3, "Жанна", "Жукова", "Александровна", 63, "Заболевание", true, 0, false),
        Patient(4, "Дмитрий", "Иванов", "Андреевич", 73, "Заболевание", false, 0, true),
        Patient(5, "Илья", "Мирослав", "Александрович", 24, "Заболевание", false, 0, true),
        Patient(6, "Максим", "Новиков", "Сергеевич", 64, "Заболевание", true, 0, true),
        Patient(7, "София", "Надибаидзе", "Христина", 54, "Заболевание", false, 0, false),
    )
)

private val patientStateOfHeathTests = listOf(
    Question(
        id = 1,
        text = "Как вы оцениваете свое самочувствие сегодня?",
        answers = listOf("Отличное", "Хорошее", "Удовлетворительное", "Плохое")
    ),
    Question(
        id = 2,
        text = "Насколько хорошо вы спали этой ночью?",
        answers = listOf("Очень хорошо", "Хорошо", "Плохо", "Очень плохо")
    ),
    Question(
        id = 3,
        text = "Какой у вас сейчас уровень энергии?",
        answers = listOf("Высокий", "Средний", "Низкий", "Очень низкий")
    ),
    Question(
        id = 4,
        text = "Как вы оцениваете свое настроение сегодня?",
        answers = listOf("Отличное", "Хорошее", "Нейтральное", "Плохое")
    ),
    Question(
        id = 5,
        text = "Были ли у вас сегодня головные боли или головокружение?",
        answers = listOf("Нет", "Легкие", "Умеренные", "Сильные")
    ),
    Question(
        id = 6,
        text = "Как вы оцениваете свой аппетит?",
        answers = listOf("Очень хороший", "Нормальный", "Пониженный", "Отсутствует")
    ),
    Question(
        id = 7,
        text = "Были ли у вас сегодня тошнота или проблемы с пищеварением?",
        answers = listOf("Нет", "Легкие", "Умеренные", "Сильные")
    ),
    Question(
        id = 8,
        text = "Как вы оцениваете уровень стресса сегодня?",
        answers = listOf("Низкий", "Средний", "Высокий", "Очень высокий")
    ),
    Question(
        id = 9,
        text = "Испытывали ли вы сегодня усталость?",
        answers = listOf("Нет", "Легкую", "Умеренную", "Сильную")
    ),
    Question(
        id = 10,
        text = "Были ли у вас затруднения с концентрацией внимания?",
        answers = listOf("Нет", "Иногда", "Часто", "Постоянно")
    ),
    Question(
        id = 11,
        text = "Как вы оцениваете свое дыхание?",
        answers = listOf("Нормальное", "Немного затруднено", "Заметно затруднено", "Очень тяжелое")
    ),
    Question(
        id = 12,
        text = "Были ли у вас сегодня боли в теле или мышечные спазмы?",
        answers = listOf("Нет", "Легкие", "Умеренные", "Сильные")
    ),
    Question(
        id = 13,
        text = "Как вы оцениваете уровень физической активности сегодня?",
        answers = listOf("Высокий", "Средний", "Низкий", "Отсутствует")
    ),
    Question(
        id = 14,
        text = "Насколько вы ощущаете эмоциональную стабильность сегодня?",
        answers = listOf("Очень стабильное состояние", "Немного нестабильное", "Нестабильное", "Очень нестабильное")
    ),
    Question(
        id = 15,
        text = "Как вы оцениваете общее состояние вашего организма на данный момент?",
        answers = listOf("Отличное", "Хорошее", "Удовлетворительное", "Плохое")
    )
)

private val patientTestsSimulationTests = listOf(
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
    ),
    Question(
        id = 5,
        text = "Как боль влияет на ваш сон?",
        answers = listOf("Не мешает сну", "Иногда мешает уснуть", "Часто прерывает сон", "Не дает спать совсем")
    ),
    Question(
        id = 6,
        text = "Как боль влияет на вашу способность выполнять повседневные задачи?",
        answers = listOf("Не влияет", "Иногда мешает", "Сильно мешает", "Полностью ограничивает")
    ),
    Question(
        id = 7,
        text = "Как часто вы принимаете обезболивающие препараты?",
        answers = listOf("Никогда", "Редко", "Регулярно", "Постоянно")
    ),
    Question(
        id = 8,
        text = "Что обычно помогает вам уменьшить боль?",
        answers = listOf("Отдых", "Массаж", "Лекарства", "Ничего не помогает")
    ),
    Question(
        id = 9,
        text = "В какое время суток боль ощущается сильнее всего?",
        answers = listOf("Утром", "Днем", "Вечером", "Ночью")
    ),
    Question(
        id = 10,
        text = "Как бы вы описали характер вашей боли?",
        answers = listOf("Тупая", "Острая", "Колющая", "Пульсирующая")
    ),
    Question(
        id = 11,
        text = "Изменяется ли боль при движении?",
        answers = listOf("Становится сильнее", "Становится слабее", "Не изменяется", "Затрудняюсь ответить")
    ),
    Question(
        id = 12,
        text = "Возникает ли боль в покое?",
        answers = listOf("Никогда", "Иногда", "Часто", "Всегда")
    ),
    Question(
        id = 13,
        text = "Насколько боль влияет на ваше настроение?",
        answers = listOf("Не влияет", "Иногда вызывает раздражение", "Часто снижает настроение", "Вызывает депрессию")
    ),
    Question(
        id = 14,
        text = "Сколько по времени вы можете оставаться активным до появления боли?",
        answers = listOf("Более часа", "30–60 минут", "10–30 минут", "Менее 10 минут")
    ),
    Question(
        id = 15,
        text = "Как вы оцениваете общее влияние боли на качество вашей жизни?",
        answers = listOf("Минимальное", "Умеренное", "Серьезное", "Критическое")
    )
)

@RequiresApi(Build.VERSION_CODES.O)
private val patientTestPreview = mutableListOf(
    PatientTestPreview(
        id = 1,
        testDate = LocalDate.now(),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_SIMULATION
    ),
    PatientTestPreview(
        id = 2,
        testDate = LocalDate.now(),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH
    ),
    PatientTestPreview(
        id = 3,
        testDate = LocalDate.now().minusDays(1),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_SIMULATION
    ),
    PatientTestPreview(
        id = 4,
        testDate = LocalDate.now().minusDays(1),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH
    ),
    PatientTestPreview(
        id = 5,
        testDate = LocalDate.now().minusDays(2),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_SIMULATION
    ),
    PatientTestPreview(
        id = 6,
        testDate = LocalDate.now().minusDays(2),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH
    ),
    PatientTestPreview(
        id = 7,
        testDate = LocalDate.now().minusDays(3),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH
    ),
    PatientTestPreview(
        id = 8,
        testDate = LocalDate.now().minusDays(4),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_SIMULATION
    ),
)