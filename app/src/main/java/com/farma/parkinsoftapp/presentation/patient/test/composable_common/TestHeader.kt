package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestHeader(
    currentQuestionIndex: Int,
    questionsCount: Int
) {
    val progress by animateFloatAsState(
        targetValue = (currentQuestionIndex + 1).toFloat() / questionsCount,
        label = "ProgressAnimation"
    )

    Row {
        Text(
            text = "Вопрос ${currentQuestionIndex + 1} из $questionsCount",
            fontSize = 13.sp,
            color = Color(0xFF49454F)
        )
        Spacer(modifier = Modifier.width(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF178399),
            trackColor = Color(0xFFEDF1F2),
            drawStopIndicator = {}
        )
    }
}