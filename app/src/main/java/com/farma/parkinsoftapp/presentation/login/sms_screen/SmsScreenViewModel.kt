package com.farma.parkinsoftapp.presentation.login.sms_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.login.sms_screen.models.SmsScreenState
import com.farma.parkinsoftapp.presentation.navigation.PatientCurrentTestRoute
import com.farma.parkinsoftapp.presentation.navigation.SmsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SmsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val route: SmsRoute = savedStateHandle.toRoute()
    private val myUserRole = route.role
    private val userId = route.userId
    private val _smsScreenState = mutableStateOf(SmsScreenState())
    val smsScreenState: State<SmsScreenState> = _smsScreenState

    private val _userRole = MutableStateFlow(UserRoleValues.UNAUTHORIZED)
    val userRole: StateFlow<UserRoleValues> = _userRole

    fun updateCodeState(newCode: String) {
        _smsScreenState.value = _smsScreenState.value.copy(smsCode = newCode)
    }

    suspend fun conform(phoneNumber: String) {
        mainRepository.setUserRole(userId, UserRoleValues.valueOf(myUserRole.name))
        _userRole.value = UserRoleValues.valueOf(myUserRole.name)
    }

    fun resetUserRole() {
        _userRole.value = UserRoleValues.UNAUTHORIZED
    }
}

private fun UserRole.convertToUserRoleValues(): UserRoleValues {
    return when(this) {
        UserRole.DOCTOR -> UserRoleValues.DOCTOR
        UserRole.PATIENT -> UserRoleValues.PATIENT
        UserRole.UNKNOWN -> UserRoleValues.UNAUTHORIZED
    }
}
