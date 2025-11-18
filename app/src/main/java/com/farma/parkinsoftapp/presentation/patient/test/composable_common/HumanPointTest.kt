package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.ButtonsGrid


@Composable
fun HumanPointTest(
    selectedPoints: List<Int>,
    humanImageType: HumanImageType,
    clickPoint: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HumanImage(humanImageType)
        PointsGreed(selectedPoints, humanImageType, clickPoint)
    }
}

@Composable
private fun HumanImage(humanImageType: HumanImageType) {
    Image(
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth(0.5f),
        painter = painterResource(
            when (humanImageType) {
                HumanImageType.FRONT -> R.drawable.front_human
                HumanImageType.BACK -> R.drawable.back_human
            }
        ),
        contentDescription = null
    )
}

@Composable
private fun PointsGreed(selectedPoints: List<Int>, humanImageType: HumanImageType, selectPoint: (Int) -> Unit) {
    Column {
        ButtonsGrid(selectedPoints, humanImageType,selectPoint)
        Box(
            Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clip(CircleShape)
                .background(
                    if (selectedPoints == listOf(0)) {
                        Color(0xFFA9E0EB)
                    } else {
                        Color(0xFFEDF1F2)
                    }
                )
                .clickable { selectPoint(0) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Не испытываю боль",
                fontSize = 14.sp
            )
        }
    }
}