package com.farma.parkinsoftapp.presentation.doctor.all_patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatientsUiState(
    val patients: List<Patient> = emptyList(),
    val filteredPatients: List<Patient> = emptyList(),
    val searchQuery: String = "",
    val selectedTab: PatientsTab = PatientsTab.OnTreatment
)

enum class PatientsTab { OnTreatment, Discharged }

@HiltViewModel
class AllPatientsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState

    init {
        viewModelScope.launch {
            mainRepository.getAllPatients().collect { patients ->
                _uiState.value = PatientsUiState(
                    patients = patients,
                    filteredPatients = patients.filter { it.onTreatment }
                )
            }
        }
    }

    suspend fun logOut() {
        mainRepository.setUserRole(UserRoleValues.UNAUTHORIZED)
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

    private fun filterPatients(tab: PatientsTab, query: String): List<Patient> {
        val baseList = if (tab == PatientsTab.OnTreatment) {
            _uiState.value.patients.filter { it.onTreatment }
        } else {
            _uiState.value.patients.filter { !it.onTreatment }
        }

        return if (query.isBlank()) baseList
        else baseList.filter {
            it.fullName.contains(query, ignoreCase = true)
        }
    }
}
