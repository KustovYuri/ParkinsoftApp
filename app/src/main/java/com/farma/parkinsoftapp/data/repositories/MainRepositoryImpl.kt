package com.farma.parkinsoftapp.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.farma.parkinsoftapp.data.local.data_store.SessionDataStore
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.httpExceptionHandler
import com.farma.parkinsoftapp.data.network.retrofit.ApiService
import com.farma.parkinsoftapp.data.network.ktor.KtorService
import com.farma.parkinsoftapp.data.network.models.DischargeModel
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.domain.utils.convertToString
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
            ktorService.getAllTestByTestPreviewId(testId, testType.value)
        }
        emit(result)
    }

    override fun getShortPatientData(): Flow<Result<ShortPatient>> = flow {
        emit(Result.Loading())
        val userId = sessionDataStore.getCurrentUserId().map { it ->
            if (it.isNotBlank()) {
                it.toLong()
            } else {
                -1
            }
        }

        val result = httpExceptionHandler {
            ktorService.getShortPatientById(userId.first())
        }
        emit(result)
    }

    override suspend fun finishSingleAnswersTest(testAnswers: List<TestAnswer>) {
        withContext(Dispatchers.IO) {
            apiService.saveTestAnswers(testAnswers)
        }
    }

    override suspend fun finishPainDetectedTest(
        testPreviewId: Long,
        test: List<PainDetectedTestQuestions>
    ): Flow<Result<Unit>> = flow {
//        emit(Result.Loading())
//        val result = httpExceptionHandler {
//            ktorService.sendNativeTest(test.convertToPainDetectedRequest(testPreviewId))
//        }
//        emit(result)
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

    override suspend fun getResultNativeTests(
        testPreviewId: Long,
    ): Flow<Result<NativeTestRequest>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.getResultNativeTest(testPreviewId)
        }
        emit(result)
    }

    override suspend fun createFinishTests(patientId: Long): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.createFinishTests(patientId)
        }
        emit(result)
    }

    override suspend fun dischargePatient(patientId: Long): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.dischargePatient(patientId)
        }
        emit(result)
    }

    override suspend fun updateDateDischarge(dischargeModel: DischargeModel): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.updateDateDischarge(dischargeModel)
        }
        emit(result)
    }

    override fun getDoctorWithPatients(): Flow<Result<DoctorWithPatientsModel>> = flow {
        emit(Result.Loading())
        val doctorId = sessionDataStore.getCurrentUserId().map { it ->
            if (it.isNotBlank()) {
                it.toLong()
            } else {
                -1
            }
        }

        val response = httpExceptionHandler {
            ktorService.getDoctorWithPatientsByDoctorId(doctorId.first())
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

    override fun getUserRole(): Flow<Pair<Long?, UserRoleValues>> {
        val userRole = sessionDataStore.getCurrentUserRole().map { it ->
            UserRoleValues.fromValue(it) ?: UserRoleValues.UNAUTHORIZED
        }

        val userId = sessionDataStore.getCurrentUserId().map { it ->
            if (it.isNotBlank()) {
                it.toLong()
            } else {
                null
            }
        }

        return userRole.combine(userId) { role, id ->
            Pair(id, role)
        }
    }

    override fun sendNativeTest(nativeTestRequest: NativeTestRequest): Flow<Result<Unit>> = flow {
        emit(Result.Loading())
        val result = httpExceptionHandler {
            ktorService.sendNativeTest(nativeTestRequest)
        }
        emit(result)
    }

    override suspend fun setUserRole(userId: Long, newUserRole: UserRoleValues) {
        sessionDataStore.setCurrentUserRole(newUserRole)
        sessionDataStore.setCurrentUserId(userId)
    }
}