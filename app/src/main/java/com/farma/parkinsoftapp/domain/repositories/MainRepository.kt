package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getPatientTests(): Flow<List<PatientTestPreview>>

    fun getShortPatientData(patientId: Long): Flow<Result<ShortPatient>>

    fun getPatientSelectedTest(testId: Long, testType: TestType): Flow<Result<List<TestModel>>>

    fun getAllPatients(): Flow<List<Patient>>

    fun getPatient(patientId: Int): Patient

    fun addNewPatient(patient: Patient): Int

    fun getUserRole(): Flow<UserRoleValues>

    suspend fun setUserRole(newUserRole: UserRoleValues)
    suspend fun finishTest(testAnswers: List<TestAnswer>)
}