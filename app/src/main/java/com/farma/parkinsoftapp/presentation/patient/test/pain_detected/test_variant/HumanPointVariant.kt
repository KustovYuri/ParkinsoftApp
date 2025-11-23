package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.HumanPointTest
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun HumanPointVariant(
    question: TestQuestion.HumanPoint,
    clickPoint: (Int) -> Unit
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    HumanPointTest(question.selectedPoints, question.type) {
        clickPoint(it)
    }
}