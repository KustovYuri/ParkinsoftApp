package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.models.DischargeModel
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.models.PainDetectedTestQuestions
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getShortPatientData(): Flow<Result<ShortPatient>>

    fun getPatientSelectedTest(testId: Long, testType: TestType): Flow<Result<List<TestModel>>>

    fun getDoctorWithPatients(): Flow<Result<DoctorWithPatientsModel>>

    fun getPatientInfo(patientId: Long): Flow<Result<LargePatientModel>>

    fun addNewPatient(patient: Patient): Flow<Result<Long>>

    fun getUserRole(): Flow<Pair<Long?, UserRoleValues>>

    suspend fun setUserRole(userId: Long, newUserRole: UserRoleValues)

    suspend fun finishSingleAnswersTest(testAnswers: List<TestAnswer>)

    suspend fun finishPainDetectedTest(
        testPreviewId: Long,
        test:  List<PainDetectedTestQuestions>
    ): Flow<Result<Unit>>

    suspend fun getResultTests(testPreviewId: Long, testType: TestType): Flow<Result<List<TestResultModel>>>
    fun sendNativeTest(nativeTestRequest: NativeTestRequest): Flow<Result<Unit>>
    suspend fun getResultNativeTests(testPreviewId: Long): Flow<Result<NativeTestRequest>>

    suspend fun createFinishTests(patientId: Long): Flow<Result<Unit>>

    suspend fun dischargePatient(patientId: Long): Flow<Result<Unit>>

    suspend fun updateDateDischarge(dischargeModel: DischargeModel): Flow<Result<Unit>>
}