package com.farma.parkinsoftapp.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CalculateAgeUseCase @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(birthDateString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val birthDate = LocalDate.parse(birthDateString, formatter)
        val currentDate = LocalDate.now()
        val period = Period.between(birthDate, currentDate)

        return period.years
    }
}