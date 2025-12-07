package com.farma.parkinsoftapp.data.network.models

import kotlinx.serialization.Serializable


@Serializable
data class LargePatientModel(
    val id: Long? = null,
    val doctorId: Long,
    val name: String,
    val secondName: String,
    val middleName: String,
    val sex: Boolean,
    val birthDate: String,
    val diagnosis: String,
    val dateReceipt: String,
    val stateHealth: String,
    val onTreatments: Boolean,
    val testsPreview: List<TestPreviewModel>,
    val lastDateAllTestsRequest: String?,
    val selectedControlTests: List<String>?,
    val dateDischarge: String?,
    val isDischarge: Boolean,
    val allControlTestsIsComplete: Boolean,
    val finalTestsIsSending: Boolean
){
    val secondNameWithInitials: String
        get() = "$secondName ${name.first()}. ${middleName.first()}."
    val initials: String
        get() = "${secondName.first()}${name.first()}".uppercase()

    val fullName: String
        get() = "$secondName ${name.first()}. ${middleName.first()}."
}

@Serializable
data class TestPreviewModel(
    val id: Long? = null,
    val patientId: Long,
    val testType: String,
    val testDate: String,
    val questionsCount: Int,
    val isViewed: Boolean? = null,
    val maxPoints: Int,
    val summaryPoints: Int,
    val progressStatus: Boolean,
    val testCompletedDate: String,
    val pf: Float? = null,
    val rp: Float? = null,
    val bp: Float? = null,
    val gh: Float? = null,
    val vt: Float? = null,
    val sf: Float? = null,
    val re: Float? = null,
    val mh: Float? = null,
)