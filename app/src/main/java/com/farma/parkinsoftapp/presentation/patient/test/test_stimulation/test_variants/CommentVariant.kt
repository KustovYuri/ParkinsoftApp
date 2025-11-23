package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun CommentVariant(
    question: TestQuestion.Comment,
    onTextChanged: (String) -> Unit,
    readOnly: Boolean = false,
) {
    if (!readOnly || question.comment.isNotBlank()) {
        Text(
            text = question.question,
            fontSize = 17.sp,
            color = Color(0xFF1C1B1F)
        )
        Spacer(modifier = Modifier.height(24.dp))
        CommentTextField(
            question.comment,
            { onTextChanged(it) },
            "Ваши изменения",
            readOnly
        )
    }
}