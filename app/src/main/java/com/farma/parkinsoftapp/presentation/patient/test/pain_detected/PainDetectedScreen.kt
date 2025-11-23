package com.farma.parkinsoftapp.presentation.patient.test.pain_detected

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.BottomBar
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.LoadingScreen
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TestHeader
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TopScreenBar
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.HumanPointVariant
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.SingleAnswersVariant
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.SlidersVariant
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

@Composable
fun PainDetectedScreen(
    viewModel: PainDetectedViewModel = hiltViewModel<PainDetectedViewModel>(),
    finishTest: () -> Unit,
    closeTest: () -> Boolean
) {
    val state by remember { viewModel.uiState }
    val currentQuestionIndex by remember { viewModel.currentQuestionIndex }
    val nextButtonIsActive by remember { viewModel.nextButtonIsActive }

    Scaffold(
        topBar = { TopScreenBar(closeTest, viewModel.testType) },
        bottomBar = {
            BottomBar(
                isSending = state.isSending,
                questionSize = state.data.size,
                currentQuestionIndex = currentQuestionIndex,
                previousQuestion = { viewModel.previousQuestion() },
                nextQuestion = {viewModel.nextQuestion()},
                enabled = nextButtonIsActive,
                finishTest = { viewModel.finishTest(finishTest) },
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
            } else if (state.error != null){
                Text(
                    text = state.error ?: "Неизвестная ошибка"
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    TestScreen(
                        state.data,
                        currentQuestionIndex,
                        state.isSending,
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
    data: List<TestQuestion>,
    currentQuestionIndex: Int,
    isSending: Boolean,
    viewModel: PainDetectedViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TestHeader(
            currentQuestionIndex = currentQuestionIndex,
            questionsCount = data.size
        )
        Spacer(modifier = Modifier.height(24.dp))
        when(val question = data[currentQuestionIndex]) {
            is TestQuestion.Graphic -> {
                GraphicVariant(
                    question,
                    {
                        viewModel.selectAnswerInGraphicAnswer(it)
                    },
                )
            }
            is TestQuestion.HumanPoint -> {
                HumanPointVariant(question, {viewModel.changeHumanPointsInHumanPointsVariant(it)})
            }
            is TestQuestion.SingleAnswer -> {
                SingleAnswersVariant(
                    question, isSending,
                    {
                        viewModel.selectAnswerInSingleAnswer(it)
                    },
                )
            }
            is TestQuestion.Slider -> { SlidersVariant(question, viewModel) }
            else -> {}
        }
    }
}