package com.farma.parkinsoftapp.data.network

import com.farma.parkinsoftapp.data.network.models.DoctorWithPatientsModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class PostService(
    private val client: HttpClient
): KtorApiService {

    override suspend fun getDoctorWithPatientsByDoctorId(doctorId: Long): DoctorWithPatientsModel {
        return try {
            client.get(HttpRoutes.POST).body<DoctorWithPatientsModel>()
        } catch (e: RedirectResponseException) {
            //3xx response
            println("ERROR: ${e.response.status.description}")
            EMPTY_STATE
        } catch (e: ClientRequestException) {
            //4xx response
            println("ERROR: ${e.response.status.description}")
            EMPTY_STATE
        } catch (e: ServerResponseException) {
            //5xx response
            println("ERROR: ${e.response.status.description}")
            EMPTY_STATE
        } catch (e: Exception) {
            //5xx response
            println("ERROR: ${e.message}")
            EMPTY_STATE
        }

    }
}

private val EMPTY_STATE = DoctorWithPatientsModel(
    id = -1,
    firstName = "",
    lastName = "",
    middleName = "",
    patients = emptyList(),
)