package com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles

sealed interface NewPatientIntent {
    data class SetSurnameIntent(val newValue: String): NewPatientIntent

    data class SetNameIntent(val newValue: String): NewPatientIntent

    data class SetMiddleNameIntent(val newValue: String): NewPatientIntent

    data class SetPhoneNumberIntent(val newValue: String): NewPatientIntent

    data class SetBirthdayIntent(val year: Int, val month: Int, val day: Int): NewPatientIntent

    data class SetDiagnosisIntent(val newValue: String): NewPatientIntent

    data class SetPatientSex(val newValue: Boolean): NewPatientIntent

    object SuccessIntent: NewPatientIntent
}