package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.HumanPointTest
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.models_common.HumanImageType
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun HumanPointVariant(
    question: TestQuestion.HumanPoint,
    changeValuePoints: (Int) -> Unit = {},
    changeValueSlider: (Int) -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    readOnly: Boolean = false,
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    if (question.humanIsEnabled) {
        HumanPointTest(question.selectedPoints, question.type) {
            changeValuePoints(it)
        }
    }
    if (question.sliderIsEnabled) {
        Slider(question.sliderValue ?: 0, readOnly) { changeValueSlider(it) }
    }
    Spacer(modifier = Modifier.height(24.dp))
    if (question.commentIsEnabled && (!readOnly || question.comment != null)) {
        CommentTextField(
            question.comment ?: "",
            { onTextChanged(it) },
            "Комментарий",
            readOnly
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ButtonsGrid(
    selectedPoints: List<Int>,
    humanImageType: HumanImageType,
    selectPoint: (Int) -> Unit
) {
    val buttonCount = when(humanImageType) {
        HumanImageType.FRONT -> (1 .. 18).toList()
        HumanImageType.BACK -> (1 .. 17).toList()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttonCount) { point ->
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selectedPoints.contains(point)) {
                            Color(0xFFA9E0EB)
                        } else {
                            Color(0xFFEDF1F2)
                        }
                    )
                    .clickable { selectPoint(point) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$point")
            }
        }
    }
}

@Composable
private fun Slider(value: Int, readOnly: Boolean, changeValue: (Int) -> Unit) {
    PercentSlider(value, readOnly) { changeValue(it) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Не изменилась", color = Color(0xFF555555), fontSize = 12.sp)
        Text("Боль пропала", color = Color(0xFF555555), fontSize = 12.sp)
    }
}