package com.farma.parkinsoftapp.domain.models

sealed interface Result<out T> {
    data class Success<out T>(val result: T): Result<T>

    data class Error(val message: String, val throwable: Throwable? = null): Result<Nothing>

    class Loading: Result<Nothing>
}