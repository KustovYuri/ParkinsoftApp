package com.farma.parkinsoftapp.presentation.doctor.new_pacient

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPatientViewModel @Inject constructor(

) : ViewModel() {

    var lastName = mutableStateOf("")
    var firstName = mutableStateOf("")
    var patronymic = mutableStateOf("")
    var phone = mutableStateOf("+7")
    var birthDate = mutableStateOf("")
    var disease = mutableStateOf("")

    var lastNameError = mutableStateOf<String?>(null)
    var firstNameError = mutableStateOf<String?>(null)
    var patronymicError = mutableStateOf<String?>(null)
    var phoneError = mutableStateOf<String?>(null)

    fun validateFields(): Boolean {
        var isValid = true

        if (lastName.value.isBlank()) {
            lastNameError.value = "Введите фамилию"
            isValid = false
        } else lastNameError.value = null

        if (firstName.value.isBlank()) {
            firstNameError.value = "Введите имя"
            isValid = false
        } else firstNameError.value = null

        if (patronymic.value.isBlank()) {
            patronymicError.value = "Введите отчество"
            isValid = false
        } else patronymicError.value = null

        if (!Regex("^\\+?\\d{11,12}\$").matches(phone.value)) {
            phoneError.value = "Некорректный номер телефона"
            isValid = false
        } else phoneError.value = null

        return isValid
    }

    fun setBirthDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        birthDate.value = format.format(calendar.time)
    }
}


