package com.farma.parkinsoftapp.presentation.doctor.patient_current_test.native_test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.domain.usecases.native_test.GetNativeTestResultUseCase
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.common.convertToScreenState
import com.farma.parkinsoftapp.presentation.navigation.PatientCurrentTestRoute
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientCurrentNativeTestViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val getNativeTestResultUseCase: GetNativeTestResultUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientCurrentTestRoute = savedStateHandle.toRoute()
    val testType = route.testType
    private val testPreviewId = route.testPreviewId
    private val _state: MutableStateFlow<ScreenState<List<TestQuestion>>> =
        MutableStateFlow(ScreenState.Loading())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getNativeTestResultUseCase(testPreviewId, testType).collect {
                _state.value = it.convertToScreenState()
            }
        }
    }
}