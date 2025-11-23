package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun SliderVariant(
    question: TestQuestion.Slider,
    onValueChange: (String, Int) -> Unit,
    changeCommentValue: (String) -> Unit,
    readOnly: Boolean = false
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    question.sliderAnswers.forEach { answer ->
        Text(
            text = answer.question,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PercentSlider(answer.value, readOnly) {
            onValueChange(answer.question, it)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Нет боли", color = Color(0xFF555555), fontSize = 12.sp)
            Text("Самая сильная боль", color = Color(0xFF555555), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
    if (question.commentIsEnabled && (!readOnly || question.comment != null)) {
        CommentTextField(
            question.comment ?: "",
            { changeCommentValue(it) },
            "Комментарий",
            readOnly
        )
    }
}