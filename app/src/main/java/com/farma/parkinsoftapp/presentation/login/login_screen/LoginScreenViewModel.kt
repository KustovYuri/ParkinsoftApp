package com.farma.parkinsoftapp.presentation.login.login_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.farma.parkinsoftapp.domain.models.validation.ValidationResult
import com.farma.parkinsoftapp.domain.usecases.validation.ValidationPhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import com.farma.parkinsoftapp.presentation.login.login_screen.models.PhoneNumberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val validationPhoneNumberUseCase: ValidationPhoneNumberUseCase,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _numberFieldState = mutableStateOf(PhoneNumberState())
    val numberFieldState: State<PhoneNumberState> = _numberFieldState

    private val _newUserRole: MutableStateFlow<UserRole?> = MutableStateFlow(null)
    val newUserRole: StateFlow<UserRole?> = _newUserRole

    fun login() {
        val validationResult = validationPhoneNumberUseCase(
            _numberFieldState.value.number
        )

        return when(validationResult) {
            is ValidationResult.Success -> {
                _numberFieldState.value = _numberFieldState.value.copy(
                    error = null
                )
                _newUserRole.value = authRepository.login(
                    _numberFieldState.value.number
                )
            }
            is ValidationResult.Error -> {
                _numberFieldState.value = _numberFieldState.value.copy(
                    error = validationResult.error
                )
            }
        }
    }

    fun setPhoneNumber(newPhoneNumber: String) {
        _numberFieldState.value = _numberFieldState.value.copy(newPhoneNumber)
    }

    fun cleanValidationIsSuccessState() {
        _newUserRole.value = null
    }
}