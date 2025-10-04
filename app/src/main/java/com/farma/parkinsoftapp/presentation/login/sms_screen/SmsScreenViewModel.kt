package com.farma.parkinsoftapp.presentation.login.sms_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.farma.parkinsoftapp.presentation.login.sms_screen.models.SmsScreenState
import javax.inject.Inject

class SmsScreenViewModel @Inject constructor(): ViewModel() {
    private val _smsScreenState = mutableStateOf(SmsScreenState())
    val smsScreenState: State<SmsScreenState> = _smsScreenState

    fun updateCodeState(newCode: String) {
        _smsScreenState.value = _smsScreenState.value.copy(smsCode = newCode)
    }
}