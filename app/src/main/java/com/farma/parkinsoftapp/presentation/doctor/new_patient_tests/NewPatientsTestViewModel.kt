package com.farma.parkinsoftapp.presentation.doctor.new_patient_tests

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class NewPatientsTestViewModel @Inject constructor(): ViewModel() {
    val controlTests = listOf("HADS", "DN4", "Освестри", "SF-36", "LANSS", "PainDetect")
    val dailyTests = listOf("Дневник тестовой стимуляции", "Дневник общего самочуствия")

    private val _selectedControlItems =  mutableStateOf(setOf<String>())
    val selectedControlItems: State<Set<String>> =  _selectedControlItems

    private val _selectedDailyItems =  mutableStateOf(setOf<String>())
    val selectedDailyItems: State<Set<String>> = _selectedDailyItems

    fun setSelectedControlItem(item: String, itemSelected: Boolean) {
        _selectedControlItems.value = if (itemSelected)
            _selectedControlItems.value - item
        else
            _selectedControlItems.value + item
    }

    fun setSelectedDailyItem(item: String, itemSelected: Boolean) {
        _selectedDailyItems.value = if (itemSelected)
            _selectedDailyItems.value - item
        else
            _selectedDailyItems.value + item
    }
}