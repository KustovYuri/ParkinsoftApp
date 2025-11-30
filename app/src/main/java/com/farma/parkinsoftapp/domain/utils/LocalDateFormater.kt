package com.farma.parkinsoftapp.domain.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
private val localDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
@RequiresApi(Build.VERSION_CODES.O)
private val localDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.convertToString(): String {
    return this.format(localDateFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertStringDateToLocalDate(stringDate: String): LocalDate {
    return LocalDate.parse(stringDate, localDateFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.convertToString(): String =
    this.format(localDateTimeFormatter)

@RequiresApi(Build.VERSION_CODES.O)
fun convertStringDateToLocalDateTime(stringDateTime: String): LocalDateTime =
    LocalDateTime.parse(stringDateTime, localDateTimeFormatter)