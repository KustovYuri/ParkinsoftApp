package com.farma.parkinsoftapp.presentation.common

import com.farma.parkinsoftapp.domain.models.Result

sealed interface ScreenState<out T> {
    data class Success<out T>(val data: T): ScreenState<T>
    class Loading(): ScreenState<Nothing>
    data class Error(val message: String, val throwable: Throwable?): ScreenState<Nothing>
}

fun <T> Result<T>.convertToScreenState(): ScreenState<T> {
    return when(this) {
        is Result.Error -> ScreenState.Error(message, throwable)
        is Result.Loading -> ScreenState.Loading()
        is Result.Success -> ScreenState.Success(result)
    }
}