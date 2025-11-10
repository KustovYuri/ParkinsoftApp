package com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.CommentTextField
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.PercentSlider
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.TestStimulationViewModel
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion

@Composable
fun HumanPointVariant(
    question: TestStimulationTestQuestion.HumanPoint,
    viewModel: TestStimulationViewModel
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
        HumanImage(question)
        PointsGreed(question) {
            viewModel.changeHumanPointsInHumanPointsVariant(it)
        }
    }
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
private fun HumanImage(question: TestStimulationTestQuestion.HumanPoint) {
    Image(
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth(0.5f),
        painter = painterResource(
            when (question.type) {
                TestStimulationTestQuestion.HumanPoint.HumanTestType.HEAD -> R.drawable.human_face
                TestStimulationTestQuestion.HumanPoint.HumanTestType.BACK -> R.drawable.human_back
            }
        ),
        contentDescription = null
    )
}

@Composable
private fun PointsGreed(question: TestStimulationTestQuestion.HumanPoint, selectPoint: (Int) -> Unit) {
    Column {
        ButtonsGrid(question, selectPoint)
        Box(
            Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clip(CircleShape)
                .background(
                    if (question.selectedPoints == listOf(0)) {
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

@Composable
fun ButtonsGrid(question: TestStimulationTestQuestion.HumanPoint, selectPoint: (Int) -> Unit) {
    val buttonCount = when(question.type) {
        TestStimulationTestQuestion.HumanPoint.HumanTestType.HEAD -> (1 .. 22).toList()
        TestStimulationTestQuestion.HumanPoint.HumanTestType.BACK -> (23 .. 45).toList()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(456.dp),
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
                        if (question.selectedPoints.contains(point)) {
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