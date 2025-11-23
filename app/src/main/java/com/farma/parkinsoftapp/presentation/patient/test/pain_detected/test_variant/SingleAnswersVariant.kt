package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun SingleAnswersVariant(
    question: TestQuestion.SingleAnswer,
    isSending: Boolean = false,
    selectAnswer: (Pair<String, Int>) -> Unit,
    readOnly: Boolean = false
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    if (question.answers.isNotEmpty()) {
        question.answers.forEach { answer ->
            val isSelected = question.selectedAnswer == answer
            if (!readOnly || isSelected) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            if (!isSending) {
                                selectAnswer(answer)
                            }
                        }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(text = answer.first, fontSize = 16.sp)
                }
            }
        }
    }
}