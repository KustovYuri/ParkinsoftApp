package com.farma.parkinsoftapp.presentation.login.login_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.farma.parkinsoftapp.domain.models.validation.ValidationResult
import com.farma.parkinsoftapp.domain.usecases.validation.ValidationPhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.domain.repositories.AuthRepository
import com.farma.parkinsoftapp.presentation.login.login_screen.models.PhoneNumberState
import com.farma.parkinsoftapp.presentation.login.login_screen.models.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val validationPhoneNumberUseCase: ValidationPhoneNumberUseCase,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _screenState = mutableStateOf(ScreenState())
    val numberFieldState: State<ScreenState> = _screenState

    private val _newUserRole: MutableStateFlow<LoginModel?> = MutableStateFlow(null)
    val newUserRole: StateFlow<LoginModel?> = _newUserRole

    fun login() {
        val validationResult = validationPhoneNumberUseCase(
            _screenState.value.data.number
        )

        when(validationResult) {
            is ValidationResult.Success -> {
                _screenState.value = _screenState.value.copy(error = null)
                doLogin()
            }
            is ValidationResult.Error -> {
                _screenState.value = _screenState.value.copy(
                    error = validationResult.error
                )
            }
        }
    }

    private fun doLogin() = viewModelScope.launch(Dispatchers.IO) {
        authRepository.login(_screenState.value.data.number).collect {
            when (it) {
                is Result.Error -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = false,
                        error = it.message
                    )
                }
                is Result.Loading -> {
                    _screenState.value = _screenState.value.copy(
                        isLoading = true
                    )
                }
                is Result.Success -> {
                    val userRole = UserRole.valueOf(it.result.role)
                    if (userRole != UserRole.UNKNOWN) {
                        _newUserRole.value = it.result
                    } else {
                        _screenState.value = _screenState.value.copy(
                            isLoading = false,
                            error = "Не удалось найти ваш номер, обратитесь к врачу для регистрации"
                        )
                    }
                }
            }
        }
    }

    fun setPhoneNumber(newPhoneNumber: String) {
        _screenState.value = _screenState.value.copy(
            data = PhoneNumberState(newPhoneNumber)
        )
    }

    fun cleanValidationIsSuccessState() {
        _newUserRole.value = null
    }
}