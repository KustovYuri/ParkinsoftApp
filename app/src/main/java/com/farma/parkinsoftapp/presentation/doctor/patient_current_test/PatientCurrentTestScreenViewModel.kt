package com.farma.parkinsoftapp.presentation.doctor.patient_current_test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.common.convertToScreenState
import com.farma.parkinsoftapp.presentation.navigation.PatientCurrentTestRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientCurrentTestScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: PatientCurrentTestRoute = savedStateHandle.toRoute()
    private val testType = route.testType
    private val testPreviewId = route.testPreviewId
    private val _state: MutableStateFlow<ScreenState<List<TestResultModel>>> =
        MutableStateFlow(ScreenState.Loading())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getResultTests(testPreviewId, testType).collect {
                _state.value = it.convertToScreenState()
            }
        }
    }
}