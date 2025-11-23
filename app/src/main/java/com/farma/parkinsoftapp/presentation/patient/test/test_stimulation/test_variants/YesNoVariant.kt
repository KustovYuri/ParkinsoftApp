package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.models_common.YesNoAnswer

@Composable
fun YesNoVariant(
    question: TestQuestion.YesNo,
    selectVariant: (String, Boolean) -> Unit,
    onTextChanged: (String) -> Unit,
    isComment: Boolean = true,
    readOnly: Boolean = false
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    question.answers.forEach { answer ->
        Text(
            text = answer.question,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnswersVariants(answer, ) { selectVariant(answer.question, it) }
        Spacer(modifier = Modifier.height(24.dp))
    }
    if (isComment && (!readOnly || question.comment.isNotBlank())) {
        CommentTextField(
            question.comment,
            { onTextChanged(it) },
            "Комментарий",
            readOnly
        )
    }
}

@Composable
fun AnswersVariants(
    answer: Pair<String, String>,
    selectVariant: (String) -> Unit
) {
    Row {
        listOf("Да", "Нет").forEach { variant ->
            val isSelected = answer.second == variant

            Box(
                modifier = Modifier
                    .width(64.dp)
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(
                        color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                    )
                    .clickable { selectVariant(variant) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = variant, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun AnswersVariants(
    answer: YesNoAnswer,
    selectVariant: (Boolean) -> Unit
) {
    Row {
        listOf("Да" to true, "Нет" to false).forEach { variant ->
            val isSelected = answer.answer == variant.second

            Box(
                modifier = Modifier
                    .width(64.dp)
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(
                        color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                    )
                    .clickable { selectVariant(variant.second) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = variant.first, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}