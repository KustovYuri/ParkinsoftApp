package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.TestStimulationViewModel
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

@Composable
fun HumanPointVariant(question: TestQuestion.HumanPoint, viewModel: TestStimulationViewModel) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Image(
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth(),
        painter = painterResource(R.drawable.human),
        contentDescription = null
    )
    if (question.sliderIsEnabled) {
        Slider(question.sliderValue ?: 0) { viewModel.changeSliderValueInHumanPoint(it) }
    }
    Spacer(modifier = Modifier.height(24.dp))
    if (question.commentIsEnabled) {
        CommentTextField(
            question.comment ?: "",
            { viewModel.changeCommentValue(it) },
            "Комментарий"
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun Slider(value: Int, changeValue: (Int) -> Unit) {
    PercentSlider(value) { changeValue(it) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Не изменилась", color = Color(0xFF555555), fontSize = 12.sp)
        Text("Боль пропала", color = Color(0xFF555555), fontSize = 12.sp)
    }
}