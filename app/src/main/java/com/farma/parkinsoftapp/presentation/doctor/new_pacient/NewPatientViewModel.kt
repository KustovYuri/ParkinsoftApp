package com.farma.parkinsoftapp.presentation.doctor.new_pacient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.mutableStateOf
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles.NewPatientState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.domain.models.validation.ValidationResult
import com.farma.parkinsoftapp.domain.usecases.CalculateAgeUseCase
import com.farma.parkinsoftapp.domain.usecases.validation.ValidationNameUseCase
import com.farma.parkinsoftapp.domain.usecases.validation.ValidationPhoneNumberUseCase
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles.NewPatientFieldState
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles.NewPatientIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NewPatientViewModel @Inject constructor(
    private val validationPhoneNumberUseCase: ValidationPhoneNumberUseCase,
    private val validationNameUseCase: ValidationNameUseCase,
    private val calculateAgeUseCase: CalculateAgeUseCase
) : ViewModel() {

    private val _newPatientState = mutableStateOf(NewPatientState())
    val newPatientState: State<NewPatientState> = _newPatientState

    private val _validationIsSuccess = MutableStateFlow(false)
    val validationIsSuccess: StateFlow<Boolean> = _validationIsSuccess

    val nextButtonIsActive = derivedStateOf {
        listOf(
            _newPatientState.value.surname.value.isNotBlank(),
            _newPatientState.value.name.value.isNotBlank(),
            _newPatientState.value.middleName.value.isNotBlank(),
            _newPatientState.value.phoneNumber.value.isNotBlank(),
            _newPatientState.value.birthday.value.isNotBlank(),
            _newPatientState.value.diagnosis.value.isNotBlank(),
            _newPatientState.value.sex != null
        ).all { it }
    }

    fun applyIntent(intent: NewPatientIntent) {
        when (intent) {
            is NewPatientIntent.SetBirthdayIntent -> setBirthDate(
                year = intent.year,
                month = intent.month,
                day = intent.day
            )

            is NewPatientIntent.SetDiagnosisIntent -> {
                _newPatientState.value = _newPatientState.value.copy(
                    diagnosis = NewPatientFieldState(
                        value = intent.newValue
                    )
                )
            }

            is NewPatientIntent.SetMiddleNameIntent -> {
                _newPatientState.value = _newPatientState.value.copy(
                    middleName = NewPatientFieldState(
                        value = intent.newValue
                    )
                )
            }

            is NewPatientIntent.SetNameIntent -> {
                _newPatientState.value = _newPatientState.value.copy(
                    name = NewPatientFieldState(
                        value = intent.newValue
                    )
                )
            }

            is NewPatientIntent.SetPhoneNumberIntent -> {
                _newPatientState.value = _newPatientState.value.copy(
                    phoneNumber = NewPatientFieldState(
                        value = intent.newValue
                    )
                )
            }

            is NewPatientIntent.SetSurnameIntent -> {
                _newPatientState.value = _newPatientState.value.copy(
                    surname = NewPatientFieldState(
                        value = intent.newValue
                    )
                )
            }

            is NewPatientIntent.SetPatientSex -> {
                _newPatientState.value = _newPatientState.value.copy(
                    sex = intent.newValue
                )
            }

            NewPatientIntent.SuccessIntent -> applyChanges()
        }
    }

    private fun setBirthDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        _newPatientState.value = _newPatientState.value.copy(
            birthday = NewPatientFieldState(
                value = format.format(calendar.time)
            )
        )
    }

    private fun applyChanges() {
        val validationIsSuccess = validateFieldState().all {
            it is ValidationResult.Success
        }

        if (validationIsSuccess) {
            _validationIsSuccess.value = true
        }
    }

    private fun validateFieldState(): List<ValidationResult> {
        val nameIsValidate = validationNameUseCase(_newPatientState.value.name.value).also {
            if (it is ValidationResult.Error) {
                _newPatientState.value = _newPatientState.value.copy(
                    name = _newPatientState.value.name.copy(error = it.error)
                )
            }
        }
        val surnameIsValidate = validationNameUseCase(_newPatientState.value.surname.value).also {
            if (it is ValidationResult.Error) {
                _newPatientState.value = _newPatientState.value.copy(
                    surname = _newPatientState.value.surname.copy(error = it.error)
                )
            }
        }
        val middleNameIsValidate =
            validationNameUseCase(_newPatientState.value.middleName.value).also {
                if (it is ValidationResult.Error) {
                    _newPatientState.value = _newPatientState.value.copy(
                        middleName = _newPatientState.value.middleName.copy(error = it.error)
                    )
                }
            }
        val phoneIsValidate =
            validationPhoneNumberUseCase(_newPatientState.value.phoneNumber.value).also {
                if (it is ValidationResult.Error) {
                    _newPatientState.value = _newPatientState.value.copy(
                        phoneNumber = _newPatientState.value.phoneNumber.copy(error = it.error)
                    )
                }
            }
        val diagnosisIsValidate = if (_newPatientState.value.diagnosis.value.isBlank()) {
            ValidationResult.Error("Поле не должно быть пустым")
        } else {
            ValidationResult.Success()
        }.also {
            if (it is ValidationResult.Error) {
                _newPatientState.value = _newPatientState.value.copy(
                    diagnosis = _newPatientState.value.diagnosis.copy(error = it.error)
                )
            }
        }

        val sexIsValidate = if (_newPatientState.value.sex == null) {
            ValidationResult.Error("Поле не должно быть пустым")
        } else {
            ValidationResult.Success()
        }

        return listOf(
            nameIsValidate,
            middleNameIsValidate,
            surnameIsValidate,
            phoneIsValidate,
            diagnosisIsValidate,
            sexIsValidate
        )
    }

    fun cleanValidationIsSuccessState() {
        _validationIsSuccess.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPatient(): Patient{
        return Patient(
            id = 0,
            firstName = _newPatientState.value.name.value,
            lastName = _newPatientState.value.surname.value,
            middleName = _newPatientState.value.middleName.value,
            age = calculateAge(_newPatientState.value.birthday.value),
            phoneNumber = _newPatientState.value.phoneNumber.value,
            birthdayDate = _newPatientState.value.birthday.value,
            diagnosis = _newPatientState.value.diagnosis.value,
            onTreatment = true,
            unreadTests = 0,
            sex = _newPatientState.value.sex ?: true
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(birthDateString: String): Int {
        return calculateAgeUseCase(birthDateString)
    }
}


