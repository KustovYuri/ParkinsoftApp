package com.farma.parkinsoftapp.presentation.login.login_screen.models

data class ScreenState(
    val data: PhoneNumberState = PhoneNumberState(),
    val isLoading: Boolean = false,
    val error: String? = null
)
