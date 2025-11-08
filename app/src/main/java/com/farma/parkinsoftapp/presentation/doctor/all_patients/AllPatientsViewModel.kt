package com.farma.parkinsoftapp.presentation.doctor.all_patients

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.PatientModel
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.domain.usecases.CalculateAgeUseCase
import com.farma.parkinsoftapp.presentation.doctor.all_patients.models.PatientsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class PatientsTab { OnTreatment, Discharged }

@HiltViewModel
class AllPatientsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val calculateAgeUseCase: CalculateAgeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState

    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private var viewModelJob: Job? = null

    init {
        getData()
    }

    fun getData() {
        viewModelJob = null
        _isRefreshing.value = true

        viewModelJob = viewModelScope.launch {
            mainRepository.getDoctorWithPatients().collect { result ->
                when (result) {
                    is Result.Error -> {
                        _isRefreshing.value = false

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }

                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                        )
                    }

                    is Result.Success -> {
                        _isRefreshing.value = false

                        _uiState.value = PatientsUiState(
                            doctorId = result.result.id ?: -1,
                            patients = result.result.patients,
                            filteredPatients = result.result.patients.filter { it.onTreatments }
                        )
                    }
                }

            }
        }
    }

    suspend fun logOut() {
        mainRepository.setUserRole(-1, UserRoleValues.UNAUTHORIZED)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = filterPatients(state.selectedTab, query)
            state.copy(searchQuery = query, filteredPatients = filtered)
        }
    }

    fun onTabSelected(tab: PatientsTab) {
        _uiState.update { state ->
            val filtered = filterPatients(tab, state.searchQuery)
            state.copy(selectedTab = tab, filteredPatients = filtered)
        }
    }

    private fun filterPatients(tab: PatientsTab, query: String): List<PatientModel> {
        val baseList = if (tab == PatientsTab.OnTreatment) {
            _uiState.value.patients.filter { it.onTreatments }
        } else {
            _uiState.value.patients.filter { !it.onTreatments }
        }

        return if (query.isBlank()) baseList
        else baseList.filter {
            it.fullName.contains(query, ignoreCase = true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(birthDateString: String): Int {
        return calculateAgeUseCase(birthDateString)
    }
}
