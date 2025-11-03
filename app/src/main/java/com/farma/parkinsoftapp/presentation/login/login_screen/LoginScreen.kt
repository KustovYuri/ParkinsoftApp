package com.farma.parkinsoftapp.presentation.login.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.presentation.login.login_screen.models.PhoneNumberState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel = hiltViewModel<LoginScreenViewModel>(),
    onNavigateToSms: (String, LoginModel) -> Unit
) {
    val screenState = remember { viewModel.numberFieldState }
    val newUserRole = viewModel.newUserRole.collectAsStateWithLifecycle()

    LaunchedEffect(newUserRole.value) {
        if (newUserRole.value != null) {
            viewModel.cleanValidationIsSuccessState()
            onNavigateToSms(
                screenState.value.data.number,
                newUserRole.value ?: LoginModel(-1, "")
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))
        Image(
            modifier = Modifier.size(140.dp, 100.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Нейротест",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(50.dp))
        PhoneNumberTextField(
            phoneNumberState = screenState.value.data,
            setPhoneNumber = { newText: String -> viewModel.setPhoneNumber(newText) }
        )
        Spacer(Modifier.height(50.dp))
        NextButton(
            isActive = screenState.value.data.number != "" || !screenState.value.isLoading,
            isLoading = screenState.value.isLoading,
            click = { viewModel.login() }
        )
        Spacer(Modifier.height(16.dp))
        if (screenState.value.error != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = screenState.value.error ?: "",
                textAlign = TextAlign.Center,
                color = Color(0xFFA82323)
            )
        }
        Spacer(Modifier.height(50.dp))
    }
}

@Composable
private fun NextButton(
    isActive: Boolean,
    isLoading: Boolean,
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
            containerColor = Color(0xFF178399),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFFE1F2F5),
            disabledContentColor = Color(0xFFB2BFC2)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFFFFFFFF)
            )
        } else {
            Text(
                text = "Продолжить",
                fontSize = 17.sp,
                fontWeight = FontWeight(400),
            )
        }
    }
}

@Composable
private fun PhoneNumberTextField(
    phoneNumberState: PhoneNumberState,
    setPhoneNumber: (String) -> Unit
) {
    Column {
        Text(
            text = "Номер телефона",
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = phoneNumberState.number,
            placeholder = {
                Text(
                    text = "+7",
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF62767A)
                )
            },
            isError = phoneNumberState.error != null,
            onValueChange = {
                setPhoneNumber(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
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
        if (phoneNumberState.error != null) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = phoneNumberState.error,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color.Red
            )
        }
    }
}