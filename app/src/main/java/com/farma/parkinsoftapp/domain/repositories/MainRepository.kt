package com.farma.parkinsoftapp.domain.repositories

import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientTest
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getPatientTests(): Flow<List<PatientTestPreview>>

    fun getPatientSelectedTest(testType: TestType): PatientTest

    fun finishTest(testId: Int)

    fun getAllPatients(): Flow<List<Patient>>

    fun getPatient(patientId: Int): Patient

    fun addNewPatient(patient: Patient): Int
}