package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithBars(
    question: TestQuestion.DisplaySlider,
    readOnly: Boolean,
    changeSliderValue: (Int) -> Unit,
) {
    val sliderValue = question.sliderValue

    val step = 5
    val max = 100
    val barsCount = max / step
    val activeBars = (sliderValue / step)

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(barsCount) { index ->
                val isActive = index < activeBars

                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height((20 + index * 4).dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isActive) Color(0xFF178399)
                            else Color(0x83178399)
                        )
                )
            }
        }
        if (!readOnly) {
            Spacer(Modifier.height(16.dp))
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = sliderValue.toFloat(),
                onValueChange = {
                    changeSliderValue((it / step).toInt() * step)
                },
                valueRange = 0f..max.toFloat(),
                steps = (max / step),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF038F8F), shape = CircleShape),
                    )
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
    }
}