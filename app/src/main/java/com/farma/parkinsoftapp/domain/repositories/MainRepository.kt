package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getShortPatientData(): Flow<Result<ShortPatient>>

    fun getPatientSelectedTest(testId: Long, testType: TestType): Flow<Result<List<TestModel>>>

    fun getDoctorWithPatients(): Flow<Result<DoctorWithPatientsModel>>

    fun getPatientInfo(patientId: Long): Flow<Result<LargePatientModel>>

    fun addNewPatient(patient: Patient): Flow<Result<Long>>

    fun getUserRole(): Flow<Pair<Long?, UserRoleValues>>

    suspend fun setUserRole(userId: Long, newUserRole: UserRoleValues)
    suspend fun finishTest(testAnswers: List<TestAnswer>)

    suspend fun getResultTests(testPreviewId: Long, testType: TestType): Flow<Result<List<TestResultModel>>>
}