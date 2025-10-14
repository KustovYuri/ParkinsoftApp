package com.farma.parkinsoftapp.domain.models.patient

data class Patient(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val age: Int,
    val diagnosis: String,
    val onTreatment: Boolean,
    val unreadTests: Int,
    val sex: Boolean
) {
    val initials: String
        get() = "${lastName.first()}${firstName.first()}".uppercase()

    val fullName: String
        get() = "$lastName ${firstName.first()}. ${middleName.first()}."
}