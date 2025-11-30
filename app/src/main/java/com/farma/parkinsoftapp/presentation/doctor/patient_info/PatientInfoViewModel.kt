package com.farma.parkinsoftapp.presentation.doctor.patient_info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.network.models.DischargeModel
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.domain.usecases.CalculateAgeUseCase
import com.farma.parkinsoftapp.domain.utils.convertToString
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.common.convertToScreenState
import com.farma.parkinsoftapp.presentation.navigation.PatientInfoRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientInfoRoute = savedStateHandle.toRoute()
    val patientId = route.patientId

    private val _patientState: MutableStateFlow<ScreenState<LargePatientModel>> =
        MutableStateFlow(ScreenState.Loading())
    val patientState: StateFlow<ScreenState<LargePatientModel>> = _patientState

    fun getPatientInfo() {
        requestToData()
    }

    private fun requestToData() {
        viewModelScope.launch {
            mainRepository.getPatientInfo(patientId).collect { result ->
                _patientState.value = result.convertToScreenState()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeDischargeDateTime(dateTime: LocalDateTime?) {
        viewModelScope.launch(Dispatchers.IO) {
            val dischargeModel = DischargeModel(
                patientId,
                dateTime?.convertToString()
            )
            mainRepository.updateDateDischarge(dischargeModel).collect {
                when(it) {
                    is Result.Error -> {
                        _patientState.value = ScreenState.Error(it.message, null)
                    }
                    is Result.Loading ->  _patientState.value = ScreenState.Loading()
                    is Result.Success -> { requestToData() }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(date: String): Int {
        return calculateAgeUseCase(date)
    }

    fun createFinalTests(){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.createFinishTests(patientId).collect {
                when(it) {
                    is Result.Error -> {
                        _patientState.value = ScreenState.Error(it.message, null)
                    }
                    is Result.Loading ->  _patientState.value = ScreenState.Loading()
                    is Result.Success -> { requestToData() }
                }
            }
        }
    }

    fun dischargePatient(){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.dischargePatient(patientId).collect {
                when(it) {
                    is Result.Error -> {
                        _patientState.value = ScreenState.Error(it.message, null)
                    }
                    is Result.Loading ->  _patientState.value = ScreenState.Loading()
                    is Result.Success -> { requestToData() }
                }
            }
        }
    }
}