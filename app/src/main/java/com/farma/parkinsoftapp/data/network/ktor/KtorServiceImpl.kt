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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val BASE_URL = "http://192.168.1.12:8080"
class KtorService(
    private val client: HttpClient
): KtorApiService {

    override suspend fun getDoctorWithPatientsByDoctorId(doctorId: Long): DoctorWithPatientsModel {
        return client.get(
            urlString = "$BASE_URL/doctor/$doctorId"
        ).body<DoctorWithPatientsModel>()
    }

    override suspend fun getShortPatientById(userId: Long): ShortPatient {
        return client.get(
            urlString = "$BASE_URL/patient/short/$userId"
        ).body<ShortPatient>()
    }

    override suspend fun getAllTestByTestPreviewId(
        testPreviewId: Long,
        testType: String
    ): List<TestModel> {
        return client.get(
            urlString = "$BASE_URL/test/allTests/$testPreviewId/$testType"
        ).body<List<TestModel>>()
    }

    override suspend fun getResultTest(
        testPreviewId: Long,
        testType: String
    ): List<TestResultModel> {
        return client.get(
            urlString = "$BASE_URL/test/getResultTests/$testPreviewId/$testType"
        ).body<List<TestResultModel>>()
    }

    override suspend fun getResultNativeTest(
        testPreviewId: Long,
    ): NativeTestRequest {
        return client.get(
            urlString = "$BASE_URL/test/getResultNativeTests/$testPreviewId"
        ).body<NativeTestRequest>()
    }

    override suspend fun createFinishTests(patientId: Long) {
        client.post(
            urlString = "$BASE_URL/doctor/createFinishTests/$patientId"
        )
    }

    override suspend fun dischargePatient(patientId: Long) {
        client.post(
            urlString = "$BASE_URL/doctor/dischargePatient/$patientId"
        )
    }

    override suspend fun updateDateDischarge(dischargeModel: DischargeModel) {
        client.post(
            urlString = "$BASE_URL/doctor/updateDateDischarge"
        ){
            contentType(ContentType.Application.Json)
            setBody(dischargeModel)
        }
    }

    override suspend fun saveSingleAnswersTestAnswers(body: List<TestAnswer>) {
        return client.post(
            urlString = "$BASE_URL/test/saveTestAnswers"
        ){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun sendNativeTest(body: NativeTestRequest) {
        return client.post(
            urlString = "$BASE_URL/test/saveNativeTest"
        ) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun createNewPatient(body: Patient): Long {
        return client.post(
            urlString = "$BASE_URL/doctor/createNewPatient"
        ){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun getDoctorPatientInfo(patientId: Long): LargePatientModel {
        return client.get(
            urlString = "$BASE_URL/doctor/patientInfo/$patientId"
        ).body<LargePatientModel>()
    }

    override suspend fun login(phoneNumber: String): LoginModel {
        return client.get(
            urlString = "$BASE_URL/login/$phoneNumber"
        ).body<LoginModel>()
    }
}