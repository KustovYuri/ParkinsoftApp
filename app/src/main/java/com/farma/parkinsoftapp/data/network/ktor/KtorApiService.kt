package com.farma.parkinsoftapp.data.network.ktor

import com.farma.parkinsoftapp.data.network.models.DischargeModel
import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.models.patient.Patient

interface KtorApiService {

    suspend fun getDoctorWithPatientsByDoctorId(doctorId: Long): DoctorWithPatientsModel

    suspend fun getShortPatientById(userId: Long): ShortPatient

    suspend fun getAllTestByTestPreviewId(testPreviewId: Long, testType: String): List<TestModel>

    suspend fun getResultTest(testPreviewId: Long, testType: String): List<TestResultModel>

    suspend fun saveSingleAnswersTestAnswers(body: List<TestAnswer>)

    suspend fun createNewPatient(body: Patient): Long

    suspend fun getDoctorPatientInfo(patientId: Long): LargePatientModel

    suspend fun login(phoneNumber: String): LoginModel
    suspend fun sendNativeTest(body: NativeTestRequest)
    suspend fun getResultNativeTest(testPreviewId: Long): NativeTestRequest

    suspend fun createFinishTests(patientId: Long)

    suspend fun dischargePatient(patientId: Long)

    suspend fun updateDateDischarge(dischargeModel: DischargeModel)


}