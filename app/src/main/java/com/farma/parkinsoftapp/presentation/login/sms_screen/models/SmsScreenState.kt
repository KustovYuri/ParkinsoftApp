package com.farma.parkinsoftapp.presentation.login.sms_screen.models

data class SmsScreenState(
    val smsCode: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)