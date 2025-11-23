package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommentTextField(
    textValue: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean
) {
    Text(
        text = placeholder,
        fontSize = 14.sp
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        enabled = !readOnly,
        modifier = Modifier
            .fillMaxWidth(),
        value = textValue,
        onValueChange = {
            onTextChanged(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEDF1F2),
            unfocusedContainerColor = Color(0xFFEDF1F2),
            unfocusedIndicatorColor = Color(0xFFEDF1F2),
            focusedIndicatorColor = Color(0xFF62767A),
            focusedTextColor = Color(0xFF002A33),
            disabledContainerColor = Color(0xFFEDF1F2),
            disabledIndicatorColor = Color(0xFFEDF1F2),
            disabledTextColor = Color(0xFF002A33)
        ),
    )
}