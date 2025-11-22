package com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion

@Composable
fun GraphicVariant(
    question: TestQuestion.Graphic,
    selectVariant: (GraphicVariant) -> Unit
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    question.graphicVariant.forEach { variant ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (question.selectedVariant == variant) {
                        Color(0xFFA9E0EB)
                    } else {
                        Color(0xFFEDF1F2)
                    },

                )
                .clickable{ selectVariant(variant) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .width(125.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = painterResource(variant.image),
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = variant.question,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(16.dp))
    }

}