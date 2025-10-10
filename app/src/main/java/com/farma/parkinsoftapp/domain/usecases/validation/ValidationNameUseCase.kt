package com.farma.parkinsoftapp.domain.usecases.validation

import com.farma.parkinsoftapp.domain.models.validation.ValidationResult
import javax.inject.Inject

class ValidationNameUseCase @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        val regex = "^[А-Яа-яЁё]+$".toRegex()
        return when {
            regex.matches(name) -> ValidationResult.Success()

            name.isBlank() -> ValidationResult.Error(error = "Поле не должно быть пустым")

            else -> ValidationResult.Error(error = "Некорректное значение поля")
        }
    }
}