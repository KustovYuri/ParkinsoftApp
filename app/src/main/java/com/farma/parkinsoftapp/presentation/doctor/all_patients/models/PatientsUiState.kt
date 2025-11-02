package com.farma.parkinsoftapp.presentation.doctor.all_patients.models

import com.farma.parkinsoftapp.domain.models.patient.PatientModel
import com.farma.parkinsoftapp.presentation.doctor.all_patients.PatientsTab

data class PatientsUiState(
    val doctorId: Long = -1,
    val patients: List<PatientModel> = emptyList(),
    val filteredPatients: List<PatientModel> = emptyList(),
    val searchQuery: String = "",
    val selectedTab: PatientsTab = PatientsTab.OnTreatment,
    val isLoading: Boolean = false,
    val error: String? = null
)