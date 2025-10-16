package com.farma.parkinsoftapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _userRole = MutableStateFlow<UserRoleValues?>(null)
    val userRole:StateFlow<UserRoleValues?> = _userRole

    init {
        getUserRole()
    }

    private fun getUserRole() {
        viewModelScope.launch {
            _userRole.value = mainRepository.getUserRole().first()
        }
    }
}