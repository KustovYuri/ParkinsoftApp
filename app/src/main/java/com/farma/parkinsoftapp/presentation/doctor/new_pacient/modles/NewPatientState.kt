package com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles

import com.farma.parkinsoftapp.presentation.doctor.new_pacient.NewPatientScreen

data class NewPatientState(
    val surname: NewPatientFieldState = NewPatientFieldState(),
    val name: NewPatientFieldState = NewPatientFieldState(),
    val middleName: NewPatientFieldState = NewPatientFieldState(),
    val phoneNumber: NewPatientFieldState = NewPatientFieldState(),
    val birthday: NewPatientFieldState = NewPatientFieldState(),
    val diagnosis: NewPatientFieldState = NewPatientFieldState(),
    val sex: Boolean? = null
)

data class NewPatientFieldState(
    val value: String = "",
    val error: String? = null
)
