package com.farma.parkinsoftapp.presentation.patient.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTestScreen(
    viewModel: PatientTestViewModel = hiltViewModel<PatientTestViewModel>(),
    closeTest: () -> Boolean,
    finishTest: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val progress by animateFloatAsState(
        targetValue = (state.currentQuestionIndex + 1).toFloat() / state.totalQuestions,
        label = "ProgressAnimation"
    )

    Scaffold(
        topBar = { TopScreenBar(closeTest) },
        bottomBar = { BottomBar(state, viewModel, finishTest) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row {
                Text(
                    text = "Вопрос ${state.currentQuestionIndex + 1} из ${state.totalQuestions}",
                    fontSize = 13.sp,
                    color = Color(0xFF49454F)
                )
                Spacer(modifier = Modifier.width(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF178399),
                    trackColor = Color(0xFFEDF1F2),
                    drawStopIndicator = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = state.question.text,
                fontSize = 17.sp,
                color = Color(0xFF1C1B1F)
            )

            Spacer(modifier = Modifier.height(24.dp))

            state.question.answers.forEach { answer ->
                val isSelected = state.selectedAnswer == answer
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { viewModel.selectAnswer(answer) }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(text = answer, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    state: Content,
    viewModel: PatientTestViewModel,
    finishTest: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 35.dp),
    ) {
        if (state.currentQuestionIndex > 0) {
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp)
                    .height(50.dp),
                onClick = { viewModel.previousQuestion() },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(width = 1.dp, color = Color(0xFF178399)),
            ) {
                Text(
                    text = "Назад",
                    color = Color(0xFF178399)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                if (state.isLastQuestion) {
                    finishTest()
                } else {
                    viewModel.nextQuestion()
                }
            },
            shape = RoundedCornerShape(12.dp),
            enabled = state.selectedAnswer != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF178399),
                contentColor = Color(0xFFFFFFFF),
                disabledContainerColor = Color(0xFFE1F2F5),
                disabledContentColor = Color(0xFFB2BFC2)
            )
        ) {
            Text(
                if (state.isLastQuestion) "Завершить" else "Далее",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(closeTest: () -> Boolean) {
    TopAppBar(
        title = {
            Text(
                text = "Дневник тестовой стимуляции",
                fontSize = 17.sp,
                color = Color(0xFF002A33)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                closeTest()
            }) {
                Icon(painterResource(R.drawable.x), contentDescription = "Закрыть")
            }
        }
    )
}