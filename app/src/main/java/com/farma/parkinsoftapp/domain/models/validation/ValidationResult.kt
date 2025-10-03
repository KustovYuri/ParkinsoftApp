package com.farma.parkinsoftapp.domain.models.validation

sealed interface ValidationResult {
    class Success(): ValidationResult
    class Error(val error: String?): ValidationResult
}