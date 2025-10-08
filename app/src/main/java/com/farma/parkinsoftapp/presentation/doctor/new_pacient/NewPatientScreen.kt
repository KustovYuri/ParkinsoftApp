package com.farma.parkinsoftapp.presentation.doctor.new_pacient

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farma.parkinsoftapp.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientScreen(
    onClose: () -> Unit = {},
    viewModel: NewPatientViewModel = hiltViewModel<NewPatientViewModel>()
) {
    val context = LocalContext.current

    // Цвета по макету
    val background = Color(0xFFF6FAFB)
    val fieldBackground = Color(0xFFEFF3F4)
    val activeButton = Color(0xFFB3E3EE)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый пациент") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(painterResource(R.drawable.x), contentDescription = "Закрыть")
                    }
                }
            )
        },
        containerColor = background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InputFieldWithLabel(
                label = "Фамилия",
                value = viewModel.lastName.value,
                onValueChange = { viewModel.lastName.value = it },
                error = viewModel.lastNameError.value,
                fieldBackground = fieldBackground
            )

            InputFieldWithLabel(
                label = "Имя",
                value = viewModel.firstName.value,
                onValueChange = { viewModel.firstName.value = it },
                error = viewModel.firstNameError.value,
                fieldBackground = fieldBackground
            )

            InputFieldWithLabel(
                label = "Отчество",
                value = viewModel.patronymic.value,
                onValueChange = { viewModel.patronymic.value = it },
                error = viewModel.patronymicError.value,
                fieldBackground = fieldBackground
            )

            InputFieldWithLabel(
                label = "Телефон",
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.value = it },
                error = viewModel.phoneError.value,
                fieldBackground = fieldBackground,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Поле даты рождения
            Text("Дата рождения", color = Color.Black)
            OutlinedTextField(
                value = viewModel.birthDate.value,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val calendar = Calendar.getInstance()
                        val dialog = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                viewModel.setBirthDate(year, month, day)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        dialog.datePicker.maxDate = System.currentTimeMillis()
                        dialog.show()
                    },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledContainerColor = fieldBackground
                ),
                placeholder = { Text("__.__.____") }
            )

            InputFieldWithLabel(
                label = "Заболевание",
                value = viewModel.disease.value,
                onValueChange = { viewModel.disease.value = it },
                fieldBackground = fieldBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.validateFields() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = activeButton,
                    contentColor = Color.Black
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Продолжить")
            }
        }
    }
}

@Composable
fun InputFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    fieldBackground: Color,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        Text(label, color = Color.Black)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = fieldBackground,
                unfocusedContainerColor = fieldBackground
            ),
            keyboardOptions = keyboardOptions
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
