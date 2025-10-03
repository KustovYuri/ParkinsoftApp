package com.farma.parkinsoftapp.domain.usecases.validation

import com.farma.parkinsoftapp.domain.models.validation.ValidationResult
import javax.inject.Inject

class ValidationPhoneNumberUseCase @Inject constructor() {
    operator fun invoke(phoneNumber: String): ValidationResult {
        val trimmedPhoneNumber = phoneNumber.trim()

        if (trimmedPhoneNumber.isEmpty()) {
            return ValidationResult.Error("Номер не может быть пустым")
        }

        val hasPlus = trimmedPhoneNumber.startsWith("+")
        val digitsPart = if (hasPlus) trimmedPhoneNumber.drop(1) else trimmedPhoneNumber

        if (!digitsPart.all { it.isDigit() }) {
            return ValidationResult.Error("Некорректный номер телефона")
        }

        if (digitsPart.length != 11) {
            return ValidationResult.Error("Некорректная длина номера")
        }

        return ValidationResult.Success()
    }
}