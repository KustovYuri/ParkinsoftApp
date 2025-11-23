package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant

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
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.PainDetectedViewModel
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun SlidersVariant(
    question: TestQuestion.Slider,
    viewModel: PainDetectedViewModel
) {
    question.sliderAnswers.forEach { slide ->
        Text(
            text = slide.question,
            fontSize = 14.sp,
            color = Color(0xFF1C1B1F)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PercentSlider(
            slide.value,
        ) { viewModel.changeSliderValueInSliderVariant(slide.question, it) }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Боли не было", color = Color(0xFF555555), fontSize = 12.sp)
            Text("Максимальная", color = Color(0xFF555555), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}