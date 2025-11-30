package com.farma.parkinsoftapp.presentation.patient.test.single_answer

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.data.network.models.TestAnswer
import com.farma.parkinsoftapp.data.network.models.TestModel
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.BottomBar
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.LoadingScreen
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TestHeader
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TopScreenBar
import com.farma.parkinsoftapp.presentation.patient.test.single_answer.models.TestScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTestScreen(
    viewModel: PatientTestViewModel = hiltViewModel<PatientTestViewModel>(),
    closeTest: () -> Boolean,
    finishTest: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val currentQuestionIndex by viewModel.currentTestQuestionId.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()

    Scaffold(
        topBar = { TopScreenBar(closeTest, viewModel.testType) },
        bottomBar = {
            BottomBar(
                isSending = state.isSending,
                questionSize = state.data.size,
                currentQuestionIndex = currentQuestionIndex,
                previousQuestion = { viewModel.previousQuestion() },
                nextQuestion = { viewModel.nextQuestion() },
                enabled = if (state.data.isNotEmpty()) {
                    selectedAnswers[state.data[currentQuestionIndex]] != null || state.data[currentQuestionIndex].answers.isEmpty()
                } else {
                    false
                },
                finishTest = { viewModel.finishTest { finishTest() } }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFF178399)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Неизвестная ошибка"
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    TestScreen(
                        currentQuestionIndex,
                        state,
                        selectedAnswers,
                        viewModel
                    )
                    if (state.isSending) {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun TestScreen(
    currentQuestionIndex: Int,
    state: TestScreenState,
    selectedAnswers: Map<TestModel, TestAnswer>,
    viewModel: PatientTestViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TestHeader(
            currentQuestionIndex,
            state.data.size
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (state.data.isNotEmpty()) {
                state.data[currentQuestionIndex].questionName
            } else {
                ""
            },
            fontSize = 17.sp,
            color = Color(0xFF1C1B1F)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.data.isNotEmpty()) {
            state.data[currentQuestionIndex].answers.forEach { answer ->
                val isSelected = selectedAnswers[state.data[currentQuestionIndex]] == answer
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            if (!state.isSending) {
                                viewModel.selectAnswer(
                                    state.data[currentQuestionIndex],
                                    answer
                                )
                            }
                        }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(text = answer.testAnswer, fontSize = 16.sp)
                }
            }
        }
    }
}

