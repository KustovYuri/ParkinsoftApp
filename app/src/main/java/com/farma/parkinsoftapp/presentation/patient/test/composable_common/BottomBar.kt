package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    isSending: Boolean,
    questionSize: Int,
    currentQuestionIndex: Int,
    previousQuestion: () -> Unit,
    nextQuestion: () -> Unit,
    enabled: Boolean,
    finishTest: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 35.dp),
    ) {
        if (currentQuestionIndex > 0) {
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp)
                    .height(50.dp),
                onClick = {
                    if (!isSending) {
                        previousQuestion()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(width = 1.dp, color = Color(0xFF178399)),
            ) {
                Text(
                    text = "Назад",
                    color = Color(0xFF178399)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (!isSending) {
                    if (currentQuestionIndex == questionSize - 1) {
                        finishTest()
                    } else {
                        nextQuestion()
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF178399),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xFFE1F2F5),
                disabledContentColor = Color(0xFFB2BFC2)
            )
        ) {
            Text(
                if (questionSize - 1 == currentQuestionIndex) "Завершить" else "Далее",
            )
        }
    }
}