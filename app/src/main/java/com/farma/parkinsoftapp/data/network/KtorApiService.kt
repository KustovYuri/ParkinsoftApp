package com.farma.parkinsoftapp.data.network

import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

interface KtorApiService {

    suspend fun getDoctorWithPatientsByDoctorId(doctorId: Long): DoctorWithPatientsModel

    companion object {
        fun create() : PostService {
            return PostService(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(
                            Json {
                                ignoreUnknownKeys = true
                                prettyPrint = true
                            }
                        )
                    }
                }
            )
        }
    }
}