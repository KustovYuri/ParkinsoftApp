package com.farma.parkinsoftapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.farma.parkinsoftapp.data.local.data_store.SessionDataStore
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.httpExceptionHandler
import com.farma.parkinsoftapp.data.network.retrofit.ApiService
import com.farma.parkinsoftapp.data.network.ktor.KtorService
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import java.time.LocalDate
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val apiService: ApiService,
    private val ktorService: KtorService
): MainRepository {

    override fun getPatientSelectedTest(testId: Long, testType: TestType): Flow<Result<List<TestModel>>> =
    flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.getShortPatientById(testId, testType.value)
        }
        emit(result)
    }

    override fun getShortPatientData(patientId: Long): Flow<Result<ShortPatient>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.getShortPatientById(patientId)
        }
        emit(result)
    }

    override suspend fun finishTest(testAnswers: List<TestAnswer>) {
        withContext(Dispatchers.IO) {
            apiService.saveTestAnswers(testAnswers)
        }
    }

    override suspend fun getResultTests(
        testPreviewId: Long,
        testType: TestType
    ): Flow<Result<List<TestResultModel>>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.getResultTest(testPreviewId, testType.value)
        }
        emit(result)
    }

    override fun getDoctorWithPatients(doctorId: Long): Flow<Result<DoctorWithPatientsModel>> = flow {
        emit(Result.Loading())
        val response = httpExceptionHandler {
            ktorService.getDoctorWithPatientsByDoctorId(doctorId)
        }
        emit(response)
    }

    override fun getPatientInfo(patientId: Long) = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.getDoctorPatientInfo(patientId)
        }
        emit(result)
    }

    override fun addNewPatient(patient: Patient): Flow<Result<Long>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.createNewPatient(patient)
        }
        emit(result)
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