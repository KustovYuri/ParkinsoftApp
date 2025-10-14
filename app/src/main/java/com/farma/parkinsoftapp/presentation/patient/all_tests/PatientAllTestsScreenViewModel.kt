package com.farma.parkinsoftapp.presentation.patient.all_tests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.patient.all_tests.models.TestPreviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class AllPreviewTestsState(
    val testsPreviewByDays: Map<LocalDate, List<PatientTestPreview>> = emptyMap()
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PatientAllTestsScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllPreviewTestsState())
    val uiState: StateFlow<AllPreviewTestsState> = _uiState

    init {
        viewModelScope.launch {
            mainRepository.getPatientTests().collect {
                convertDataToState(it)
            }
        }
    }

    fun convertDataToState(data: List<PatientTestPreview>){
        val resultMap: Map<LocalDate, List<PatientTestPreview>> = data
            .groupBy { it.testDate }
            .toSortedMap (compareByDescending { it })

        _uiState.value = _uiState.value.copy(resultMap)
    }

    fun convertLocalDateToUiDate(dateLabel: LocalDate): String {
        val dayOfMonth = dateLabel.dayOfMonth
        val month = dateLabel
            .month
            .getDisplayName(
                TextStyle.FULL,
                Locale("ru", "RU")
            )
        val dayOfWeek = dateLabel
            .dayOfWeek
            .getDisplayName(
                TextStyle.SHORT,
                Locale("ru", "RU")
            )
            .uppercase()

        return "$dayOfWeek $dayOfMonth $month"
    }
}
