package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PercentSlider(value: Int, readOnly: Boolean = false, onValueChange: (Int) -> Unit) {
    var sliderPos by remember { mutableFloatStateOf(value.toFloat()) }

    Slider(
        modifier = Modifier
            .fillMaxWidth(),
        value = value.toFloat(),
        onValueChange = {
            if (!readOnly) {
                val stepped = (it / 5).roundToInt() * 5
                sliderPos = stepped.toFloat()
                onValueChange(stepped)
            }
        },
        valueRange = 0f..100f,
        thumb = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF038F8F), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sliderPos.toInt().toString(),
                    color = Color.White,
                )
            }
        },
        track = { sliderState ->
            val fraction by remember {
                derivedStateOf {
                    (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                }
            }

            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .align(Alignment.CenterStart)
                        .height(6.dp)
                        .background(Color(0xFF038F8F), shape = CircleShape),
                )
                Box(
                    Modifier
                        .fillMaxWidth(1f - fraction)
                        .align(Alignment.CenterEnd)
                        .height(4.dp)
                        .background(Color(0xFF768080), CircleShape)
                )
            }
        }
    )
}