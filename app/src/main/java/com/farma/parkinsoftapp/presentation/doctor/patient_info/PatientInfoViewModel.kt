package com.farma.parkinsoftapp.presentation.doctor.patient_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.navigation.PatientInfoRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientInfoRoute = savedStateHandle.toRoute()
    val patientId = route.patientId

    private val _patient = MutableStateFlow(
        Patient(1, "Мария", "Жукова", "Дмитриевна", 52, "Заболевание", true, 10, false),
    )
    val patient: StateFlow<Patient> = _patient

    init {
        _patient.value = mainRepository.getPatient(patientId)
    }
}