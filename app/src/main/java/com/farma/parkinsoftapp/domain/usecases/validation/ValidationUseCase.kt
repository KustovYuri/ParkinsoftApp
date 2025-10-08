package com.farma.parkinsoftapp.domain.usecases.validation

import javax.inject.Inject


class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(name: String): Boolean {
        return name.matches(Regex("^[А-ЯЁ][а-яё-]{1,30}\$"))
    }
}

class ValidatePhoneUseCase @Inject constructor() {
    operator fun invoke(phone: String): Boolean {
        return phone.matches(Regex("^\\+7\\s?\\d{3}\\s?\\d{3}-?\\d{2}-?\\d{2}\$"))
    }
}

class ValidateBirthDateUseCase @Inject constructor() {
    operator fun invoke(date: String): Boolean {
        return date.matches(Regex("^\\d{2}\\.\\d{2}\\.\\d{4}\$"))
    }
}