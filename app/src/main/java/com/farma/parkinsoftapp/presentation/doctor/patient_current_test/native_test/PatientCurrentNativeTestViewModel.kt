package com.farma.parkinsoftapp.presentation.doctor.patient_current_test.native_test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.data.raw_native_tests.getDN4TestData
import com.farma.parkinsoftapp.data.raw_native_tests.getPainDetectedTestData
import com.farma.parkinsoftapp.data.raw_native_tests.getSF36TestData
import com.farma.parkinsoftapp.data.raw_native_tests.getTestStimulationTestData
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.common.convertToScreenState
import com.farma.parkinsoftapp.presentation.navigation.PatientCurrentTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PatientCurrentNativeTestViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientCurrentTestRoute = savedStateHandle.toRoute()
    private val testType = route.testType
    private val testPreviewId = route.testPreviewId
    private val _state: MutableStateFlow<ScreenState<List<TestQuestion>>> =
        MutableStateFlow(ScreenState.Success(
            when(testType) {
                TestType.TEST_STIMULATION_DIARY -> getTestStimulationTestData()
                TestType.DN4 -> getDN4TestData()
                TestType.SF36 -> getSF36TestData()
                TestType.PAIN_DETECTED -> getPainDetectedTestData()
                else -> emptyList()
            }
        ))
    val state = _state.asStateFlow()
}