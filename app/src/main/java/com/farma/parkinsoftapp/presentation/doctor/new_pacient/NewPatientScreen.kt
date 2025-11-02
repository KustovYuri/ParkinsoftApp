package com.farma.parkinsoftapp.presentation.doctor.new_pacient

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.domain.models.patient.Patient
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.modles.NewPatientIntent
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientScreen(
    viewModel: NewPatientViewModel = hiltViewModel<NewPatientViewModel>(),
    closeScreen: () -> Unit,
    nextScreenNavigation: (Patient) -> Unit
) {
    val context = LocalContext.current
    val spacerHeightModifier = Modifier.height(24.dp)
    val screenState by remember { viewModel.newPatientState }
    val validationIsSuccess = viewModel.validationIsSuccess.collectAsStateWithLifecycle()
    val nextButtonIsActive by remember { viewModel.nextButtonIsActive }

    LaunchedEffect(validationIsSuccess.value) {
        if (validationIsSuccess.value) {
            viewModel.cleanValidationIsSuccessState()
            nextScreenNavigation(viewModel.getPatient())
        }
    }

    Scaffold(
        topBar = { TopScreenBar(closeScreen) },
        containerColor = Color(0xFFFFFFFF)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            InputFieldWithLabel(
                label = "Фамилия",
                value = screenState.surname.value,
                onValueChange = {
                    viewModel.applyIntent(NewPatientIntent.SetSurnameIntent(it))
                },
                error = screenState.surname.error
            )
            Spacer(spacerHeightModifier)
            InputFieldWithLabel(
                label = "Имя",
                value = screenState.name.value,
                onValueChange = {
                    viewModel.applyIntent(NewPatientIntent.SetNameIntent(it))
                },
                error = screenState.name.error
            )
            Spacer(spacerHeightModifier)
            InputFieldWithLabel(
                label = "Отчество",
                value = screenState.middleName.value,
                onValueChange = {
                    viewModel.applyIntent(NewPatientIntent.SetMiddleNameIntent(it))
                },
                error = screenState.middleName.error
            )
            Spacer(spacerHeightModifier)
            GenderSelector(screenState.sex) {
                viewModel.applyIntent(NewPatientIntent.SetPatientSex(it))
            }
            Spacer(spacerHeightModifier)
            InputFieldWithLabel(
                label = "Телефон",
                value = screenState.phoneNumber.value,
                onValueChange = {
                    viewModel.applyIntent(NewPatientIntent.SetPhoneNumberIntent(it))
                },
                error = screenState.phoneNumber.error,
                keyboardType = KeyboardType.Phone
            )
            Spacer(spacerHeightModifier)
            BirthdayField(
                value = screenState.birthday.value,
                context = context,
                onValueChange = { year, month, day ->
                    viewModel.applyIntent(NewPatientIntent.SetBirthdayIntent(year, month, day))
                }
            )
            Spacer(spacerHeightModifier)
            InputFieldWithLabel(
                label = "Заболевание",
                value = screenState.diagnosis.value,
                onValueChange = {
                    viewModel.applyIntent(NewPatientIntent.SetDiagnosisIntent(it))
                },
                error = screenState.diagnosis.error,
            )
            Spacer(spacerHeightModifier)
            NextButton(
                isActive = nextButtonIsActive,
                click = {
                    viewModel.applyIntent(NewPatientIntent.SuccessIntent)
                }
            )
        }
    }
}

@Composable
private fun GenderSelector(
    selectedGender: Boolean?,
    onGenderSelected: (Boolean) -> Unit
) {
    Column {
        Text(
            text = "Пол",
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender != null  && selectedGender,
                    onClick = { onGenderSelected(true) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF239FB6)
                    )
                )
                Text(text = "Мужской")
            }
            Spacer(Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender != null && !selectedGender,
                    onClick = { onGenderSelected(false) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF239FB6)
                    )
                )
                Text(text = "Женский")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(onClose: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Новый пациент",
                fontSize = 17.sp,
                color = Color(0xFF002A33),
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(painterResource(R.drawable.x), contentDescription = "Закрыть")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}

@Composable
private fun NextButton(
    isActive: Boolean,
    click: () -> Unit,
) {
    TextButton(
        enabled = isActive,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = click,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFA9E0EB),
            contentColor = Color(0xFF002A33),
            disabledContainerColor = Color(0xFFEDF1F2),
            disabledContentColor = Color(0xFFB2BFC2)
        )
    ) {
        Text(
            text = "Продолжить",
            fontSize = 17.sp,
            fontWeight = FontWeight(400),
        )
    }
}

@Composable
private fun BirthdayField(
    value: String,
    onValueChange: (year: Int, month: Int, day: Int) -> Unit,
    context: Context,
) {
    Column {
        Text(
            text = "Дата рождения",
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val calendar = Calendar.getInstance()
                    val dialog = DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            onValueChange(year, month, day)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    dialog.datePicker.maxDate = System.currentTimeMillis()
                    dialog.show()
                },
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color(0xFFEDF1F2),
                disabledContainerColor = Color(0xFFEDF1F2),
                disabledTextColor = Color(0xFF002A33),
                focusedContainerColor = Color(0xFFEDF1F2),
                unfocusedContainerColor = Color(0xFFEDF1F2),
                unfocusedIndicatorColor = Color(0xFFEDF1F2),
                focusedIndicatorColor = Color(0xFF62767A),
                focusedTextColor = Color(0xFF002A33)
            ),
            placeholder = { Text("__.__.____") }
        )
    }
}

@Composable
fun InputFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            isError = error != null,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEDF1F2),
                unfocusedContainerColor = Color(0xFFEDF1F2),
                unfocusedIndicatorColor = Color(0xFFEDF1F2),
                focusedIndicatorColor = Color(0xFF62767A),
                focusedTextColor = Color(0xFF002A33)
            ),
        )
        if (error != null) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = error,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color.Red
            )
        }
    }
}
