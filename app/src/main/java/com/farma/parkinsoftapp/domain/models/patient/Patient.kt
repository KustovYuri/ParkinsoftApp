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

data class PatientModel(
    val id: Long? = null,
    val doctorId: Long,
    val name: String,
    val secondName: String,
    val middleName: String,
    val phoneNumber: String,
    val sex: Boolean,
    val birthDate: String,
    val diagnosis: String,
    val dateReceipt: String,
    val stateHealth: String,
    val onTreatments: Boolean,
    val unreadTests: Int,
) {
    val initials: String
        get() = "${secondName.first()}${name.first()}".uppercase()

    val fullName: String
        get() = "$secondName ${name.first()}. ${middleName.first()}."
}
