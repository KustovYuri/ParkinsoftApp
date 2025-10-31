package com.farma.parkinsoftapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.farma.parkinsoftapp.data.local.data_store.SessionDataStore
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.ApiService
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import java.time.LocalDate
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val apiService: ApiService
): MainRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getPatientTests(): Flow<List<PatientTestPreview>> = flow {
        emit(patientTestPreview)
    }

    override fun getPatientSelectedTest(testId: Long, testType: TestType): Flow<Result<List<TestModel>>> =
    flow {
        emit(Result.Loading())
        try {
            val testData = apiService
                .getShortPatientById(
                testId, testType.value
                ).body() ?: throw IOException()

            emit(Result.Success(testData))
        } catch (throwable: Throwable) {
            emit(
                Result.Error("Ошибка запроса данных теста", throwable)
            )
        }
    }

    override fun getShortPatientData(patientId: Long): Flow<Result<ShortPatient>> = flow {
        emit(Result.Loading())
        try {
            val shortPatient = apiService
                .getShortPatientById(
                    userId = patientId
                ).body() ?: throw IOException()

            emit(Result.Success(shortPatient))
        } catch (e: Throwable) {
            emit(
                Result.Error("Ошибка запроса данных пациента", e)
            )
        }
    }

    override suspend fun finishTest(testAnswers: List<TestAnswer>) {
        withContext(Dispatchers.IO) {
            apiService.saveTestAnswers(testAnswers)
        }
    }

    override fun getDoctorWithPatients(doctorId: Long): Flow<Result<DoctorWithPatientsModel>> = flow {
        emit(Result.Loading())
        try {
            val response = apiService
                .getDoctorWithPatientsByDoctorId(doctorId)
                .body() ?: throw IOException()

            emit(Result.Success(response))
        }catch (throwable: Throwable) {
            emit(Result.Error("Ошибка получения информации о пациентах", throwable))
        }
    }

    override fun getPatientInfo(patientId: Long) = flow {
        emit(Result.Loading())
        try {
            val result = apiService.getDoctorPatientInfo(patientId).body() ?: throw IOException()
            emit(Result.Success(result))
        }catch (e: Throwable) {
            emit(Result.Error("Ошибка получения данных пациента", e))
        }
    }

    override fun addNewPatient(patient: Patient): Long {
        val patientId = doctorPatients.value.size + 2

        doctorPatients.value = doctorPatients.value + patient.copy(id = patientId)
        return patientId.toLong()
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

@RequiresApi(Build.VERSION_CODES.O)
private val patientTestPreview = mutableListOf(
    PatientTestPreview(
        id = 1,
        testDate = LocalDate.now(),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_STIMULATION_DIARY
    ),
    PatientTestPreview(
        id = 2,
        testDate = LocalDate.now(),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH_DIARY
    ),
    PatientTestPreview(
        id = 3,
        testDate = LocalDate.now().minusDays(1),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_STIMULATION_DIARY
    ),
    PatientTestPreview(
        id = 4,
        testDate = LocalDate.now().minusDays(1),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH_DIARY
    ),
    PatientTestPreview(
        id = 5,
        testDate = LocalDate.now().minusDays(2),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_STIMULATION_DIARY
    ),
    PatientTestPreview(
        id = 6,
        testDate = LocalDate.now().minusDays(2),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH_DIARY
    ),
    PatientTestPreview(
        id = 7,
        testDate = LocalDate.now().minusDays(3),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник общего самочувствия",
        isSuccessTest = false,
        testType = TestType.STATE_OF_HEALTH_DIARY
    ),
    PatientTestPreview(
        id = 8,
        testDate = LocalDate.now().minusDays(4),
        questionCount = 10,
        testTime = 15,
        testName = "Дневник тестовой стимуляции",
        isSuccessTest = false,
        testType = TestType.TEST_STIMULATION_DIARY
    ),
)