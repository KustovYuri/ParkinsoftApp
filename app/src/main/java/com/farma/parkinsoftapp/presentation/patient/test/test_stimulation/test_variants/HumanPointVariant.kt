package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

@Composable
fun ColumnScope.HumanPointVariant(question: TestQuestion.HumanPoint) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Image(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        painter = painterResource(R.drawable.human),
        contentDescription = null
    )
    if (question.sliderIsEnabled) {
        var value by remember { mutableFloatStateOf(0f) }

        Slider(
            value = value,
            onValueChange = { value = it },
            steps = 5,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00838F),
                activeTrackColor = Color(0xFF00838F),
                inactiveTrackColor = Color.LightGray
            ),
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    if (question.commentIsEnabled) {

    }
    Spacer(modifier = Modifier.height(24.dp))
}