package com.farma.parkinsoftapp.presentation.doctor.new_patient_tests

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.patient.AllTestsTypes
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.common.convertToScreenState
import com.farma.parkinsoftapp.presentation.navigation.NewPatientTestsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class NewPatientsTestViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _screenState = mutableStateOf<ScreenState<Long>?>(null)
    val screenState: State<ScreenState<Long>?> = _screenState

    private val _selectedControlItems =  mutableStateOf(setOf<AllTestsTypes>())
    val selectedControlItems: State<Set<AllTestsTypes>> =  _selectedControlItems

    private val _selectedDailyItems =  mutableStateOf(setOf<AllTestsTypes>())
    val selectedDailyItems: State<Set<AllTestsTypes>> = _selectedDailyItems

    val createButtonIsActive = derivedStateOf {
        _selectedDailyItems.value.isNotEmpty() || _selectedControlItems.value.isNotEmpty()
    }

    fun setSelectedControlItem(item: AllTestsTypes, itemSelected: Boolean) {
        _selectedControlItems.value = if (itemSelected)
            _selectedControlItems.value - item
        else
            _selectedControlItems.value + item
    }

    fun setSelectedDailyItem(item: AllTestsTypes, itemSelected: Boolean) {
        _selectedDailyItems.value = if (itemSelected)
            _selectedDailyItems.value - item
        else
            _selectedDailyItems.value + item
    }

    fun createPatient(
        navigationArgs: NewPatientTestsRoute
    ) {
        viewModelScope.launch {
            mainRepository.addNewPatient(navigationArgs.convertToPatient()).collect {
                _screenState.value = it.convertToScreenState()
            }
        }
    }

    private fun NewPatientTestsRoute.convertToPatient(): Patient {
        return Patient(
            this.id,
            this.doctorId,
            this.firstName,
            this.lastName,
            this.middleName,
            this.age,
            this.phoneNumber,
            this.birthdayDate,
            this.diagnosis,
            this.onTreatment,
            this.unreadTests,
            this.sex,
            _selectedDailyItems.value.convertToTypeList(),
            _selectedControlItems.value.convertToTypeList()
        )
    }

    private fun Set<AllTestsTypes>.convertToTypeList(): List<String> {
        return this.toList().map { it.testType }
    }
}