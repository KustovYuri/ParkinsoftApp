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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
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
        PhoneNumberTextField()
        Spacer(Modifier.height(50.dp))
        NextButton()
        Spacer(Modifier.height(50.dp))
    }
}

@Composable
private fun NextButton() {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF178399),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFFE1F2F5),
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
private fun PhoneNumberTextField() {
    val textValue = remember { mutableStateOf("") }

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
            value = textValue.value,
            placeholder = {
                Text(
                    text = "+7",
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF62767A)
                )
            },
            onValueChange = {
                textValue.value = it
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
    }
}