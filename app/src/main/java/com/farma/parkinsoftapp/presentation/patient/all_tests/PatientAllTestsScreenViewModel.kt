package com.farma.parkinsoftapp.presentation.patient.all_tests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.farma.parkinsoftapp.presentation.patient.all_tests.models.TestPreviewModel
import com.farma.parkinsoftapp.presentation.patient.all_tests.models.QuestionnaireStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class AllPreviewTestsState(
    val testsPreviewByDays: Map<LocalDate, List<TestPreviewModel>> = emptyMap()
)

@RequiresApi(Build.VERSION_CODES.O)
class PatientAllTestsScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AllPreviewTestsState())
    val uiState: StateFlow<AllPreviewTestsState> = _uiState

    init {
        convertDataToState(loadMockData())
    }

    fun convertDataToState(data: List<TestPreviewModel>){
        val resultMap: Map<LocalDate, List<TestPreviewModel>> = data
            .groupBy { it.dateLabel }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMockData(): List<TestPreviewModel> {
        return listOf(
            TestPreviewModel(
                1,
                "Дневник тестовой стимуляции",
                10,
                15,
                QuestionnaireStatus.FILLED,
                LocalDate.now()
            ),
            TestPreviewModel(
                2,
                "Дневник тестовой стимуляции",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now()
            ),
            TestPreviewModel(
                3,
                "Дневник общего самочувствия",
                10,
                15,
                QuestionnaireStatus.FILLED,
                LocalDate.now()
            ),
            TestPreviewModel(
                4,
                "Дневник общего самочувствия",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now()
            ),
            TestPreviewModel(
                5,
                "Дневник тестовой стимуляции",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(1)
            ),
            TestPreviewModel(
                6,
                "Дневник общего самочувствия",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(1)
            ),
            TestPreviewModel(
                7,
                "Дневник тестовой стимуляции",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(1)
            ),
            TestPreviewModel(
                8,
                "Дневник общего самочувствия",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(2)
            ),
            TestPreviewModel(
                9,
                "Дневник тестовой стимуляции",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(2)
            ),
            TestPreviewModel(
                10,
                "Дневник общего самочувствия",
                10,
                15,
                QuestionnaireStatus.NEEDS_FILL,
                LocalDate.now().minusDays(3)
            )
        )
    }
}
