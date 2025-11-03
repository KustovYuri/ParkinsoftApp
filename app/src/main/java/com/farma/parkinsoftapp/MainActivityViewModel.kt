package com.farma.parkinsoftapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _userRole = MutableStateFlow<UserRoleValues?>(null)
    val userRole:StateFlow<UserRoleValues?> = _userRole

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        getUserRoleWithId()
    }

    private fun getUserRoleWithId() {
        viewModelScope.launch {
            _userRole.value = mainRepository.getUserRole().first().second
            _isLoading.value = false
        }
    }
}