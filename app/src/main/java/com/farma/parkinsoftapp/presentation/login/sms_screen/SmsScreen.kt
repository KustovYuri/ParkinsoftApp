package com.farma.parkinsoftapp.presentation.login.sms_screen

import com.farma.parkinsoftapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    phoneNumber: String = "+7 902 488-73-66"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(24.dp))
        TopBar()
        Spacer(Modifier.height(83.dp))
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = "Мы отправили SMS на номер\n$phoneNumber\nВведите полученный код",
            fontSize = 17.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(32.dp))
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = "Код подтверждения",
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        )
        Spacer(Modifier.height(8.dp))
        SmsCodeInput(
            modifier = Modifier.padding(start = 20.dp)
        ){}
    }
}

@Composable
private fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(2.dp))
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = null
            )
        }
        Text(
            text = "Код подтверждения",
            fontSize = 17.sp,
            fontWeight = FontWeight(500),
        )
    }
}

@Composable
private fun SmsCodeInput(
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
    dividerPosition: Int = 3,
    onCodeEntered: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    BasicTextField(
        modifier = modifier,
        value = code,
        onValueChange = {
            if (it.length <= codeLength && it.all { ch -> ch.isDigit() }) {
                code = it
                if (it.length == codeLength) {
                    onCodeEntered(it)
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                code.padEnd(codeLength, ' ').forEachIndexed { index, char ->
                    if (dividerPosition > 0 && index == dividerPosition) {
                        Text(
                            text = "-",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .width(50.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF62767A),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDF1F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.takeIf { it != ' ' }?.toString() ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    )

}