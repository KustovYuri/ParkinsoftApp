package com.farma.parkinsoftapp.data.network

import com.farma.parkinsoftapp.domain.models.Result
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

suspend fun <T> httpExceptionHandler(
    httpAction: suspend  () -> T
): Result<T> {
    return try {
        val result = httpAction()
        Result.Success(result)
    } catch (e: RedirectResponseException) {
        //3xx response
        Result.Error("ERROR: ${e.response.status.description}")
    } catch (e: ClientRequestException) {
        //4xx response
        Result.Error("ERROR: ${e.response.status.description}")
    } catch (e: ServerResponseException) {
        //5xx response
        Result.Error("ERROR: ${e.response.status.description}")
    } catch (e: Exception) {
        //5xx response
        Result.Error("ERROR: ${e.message}")
    }
}