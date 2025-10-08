package com.farma.parkinsoftapp.presentation.doctor.all_patients

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class Patient(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val age: Int,
    val disease: String,
    val onTreatment: Boolean,
    val unreadTests: Int,
    val sex: Boolean
) {
    val initials: String
        get() = "${lastName.first()}${firstName.first()}".uppercase()
    val fullName: String
        get() = "$lastName ${firstName.first()}. ${patronymic.first()}."
}

data class PatientsUiState(
    val patients: List<Patient> = emptyList(),
    val filteredPatients: List<Patient> = emptyList(),
    val searchQuery: String = "",
    val selectedTab: PatientsTab = PatientsTab.OnTreatment
)

enum class PatientsTab { OnTreatment, Discharged }

@HiltViewModel
class PatientsViewModel @Inject constructor() : ViewModel() {

    private val mockPatients = listOf(
        Patient(1, "Мария", "Жукова", "Христина", 63, "Заболевание", true, 10, false),
        Patient(2, "Михаил", "Миронов", "Андреевич", 63, "Заболевание", true, 10, true),
        Patient(3, "Жанна", "Жукова", "Христина", 63, "Заболевание", true, 0, false),
        Patient(4, "Михаил", "Миронов", "Андреевич", 63, "Заболевание", false, 0, true),
        Patient(5, "Мария", "Миронова", "Александровна", 63, "Заболевание", false, 5, false),
        Patient(6, "Максим", "Миронов", "Сергеевич", 63, "Заболевание", true, 0, true),
        Patient(7, "Жанна", "Жукова", "Христина", 63, "Заболевание", false, 0, false),
    )

    private val _uiState = MutableStateFlow(
        PatientsUiState(
            patients = mockPatients,
            filteredPatients = mockPatients.filter { it.onTreatment }
        )
    )
    val uiState: StateFlow<PatientsUiState> = _uiState

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
            mockPatients.filter { it.onTreatment }
        } else {
            mockPatients.filter { !it.onTreatment }
        }

        return if (query.isBlank()) baseList
        else baseList.filter {
            it.fullName.contains(query, ignoreCase = true)
        }
    }
}
