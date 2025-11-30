package com.farma.parkinsoftapp.presentation.patient.all_tests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.data.network.models.TestPreview
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class AllPreviewTestsState(
    val testsPreviewByDays: Map<LocalDate, List<PatientTestPreview>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PatientAllTestsScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllPreviewTestsState())
    val uiState: StateFlow<AllPreviewTestsState> = _uiState

    private val _shortPatient: MutableStateFlow<ShortPatient?> = MutableStateFlow(null)
    val shortPatient = _shortPatient.asStateFlow()

    init {
        viewModelScope.launch {
            mainRepository.getShortPatientData().collect {
                when (it) {
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                        )
                    }
                    is Result.Success -> {
                        _shortPatient.value = it.result
                        convertDataToState(it.result)
                    }
                }
            }
        }
    }

    suspend fun logOut() {
        mainRepository.setUserRole(-1,UserRoleValues.UNAUTHORIZED)
    }

    fun convertDataToState(shortPatient: ShortPatient) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
        val resultMap: Map<LocalDate, List<PatientTestPreview>> = shortPatient.testPreview
            .map {
                PatientTestPreview(
                    id = it.id ?: 0,
                    testDate = LocalDate.parse(it.testDate, formatter),
                    questionCount = it.questionsCount,
                    testTime = it.testTime,
                    testName = it.getTestName(),
                    isSuccessTest = it.isCompleted,
                    testType = TestType.fromString(it.testType) ?: TestType.TEST_STIMULATION_DIARY
                )
            }
            .groupBy { it.testDate }
            .toSortedMap(compareByDescending { it })

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            testsPreviewByDays = resultMap
        )
    }

    private fun TestPreview.getTestName(): String {
        return when (this.testType) {
            "test_stimulation_diary" -> "Дневник тестовой стимуляции"
            "hads1" -> "HADS 1"
            "hads2" -> "HADS 2"
            "osvestry" -> "Освестри"
            "lanss" -> "LANSS"
            "dn4" -> "DN-4"
            "sf36" -> "SF-36"
            "pain_detected" -> "Pain Detected"
            else -> "Неизвестный тест"
        }
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
