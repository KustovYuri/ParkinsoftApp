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
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.TestStimulationViewModel
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

@Composable
fun SliderVariant(
    question: TestQuestion.Slider,
    viewModel: TestStimulationViewModel
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    question.sliderAnswers.forEach { answer ->
        Text(
            text = answer.first,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PercentSlider(answer.second) {
            viewModel.changeSliderValueInSliderVariant(answer.first, it)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
    if (question.commentIsEnabled) {
        CommentTextField(
            question.comment ?: "",
            { viewModel.changeCommentValue(it) },
            "Комментарий"
        )
    }
}