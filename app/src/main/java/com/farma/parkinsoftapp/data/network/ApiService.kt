package com.farma.parkinsoftapp.data.network

import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.LoginRequest
import com.farma.parkinsoftapp.data.network.models.LoginResponse
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    @GET("/patient/short/{patientId}")
    suspend fun getShortPatientById(
        @Path("patientId") userId: Long
    ): Response<ShortPatient>

    @GET("/test/allTests/{testPreviewId}/{testType}")
    suspend fun getShortPatientById(
        @Path("testPreviewId") testPreviewId: Long,
        @Path("testType") testType: String
    ): Response<List<TestModel>>

    @GET("/doctor/{doctorId}")
    suspend fun getDoctorWithPatientsByDoctorId(
        @Path("doctorId") doctorId: Long,
    ): Response<DoctorWithPatientsModel>

    @POST("/test/saveTestAnswers")
    suspend fun saveTestAnswers(
        @Body body: List<TestAnswer>
    )

    @GET("/doctor/patientInfo/{patientId}")
    suspend fun getDoctorPatientInfo(
        @Path("patientId") patientId: Long,
    ): Response<LargePatientModel>
}