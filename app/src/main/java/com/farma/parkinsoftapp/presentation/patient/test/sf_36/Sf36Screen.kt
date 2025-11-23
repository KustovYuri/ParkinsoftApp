package com.farma.parkinsoftapp.presentation.patient.test.sf_36

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.BottomBar
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.LoadingScreen
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TestHeader
import com.farma.parkinsoftapp.presentation.patient.test.composable_common.TopScreenBar
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.AnswersVariants

@Composable
fun Sf36Screen(
    viewModel: Sf36ViewModel = hiltViewModel<Sf36ViewModel>(),
    finishTest: () -> Unit,
    closeTest: () -> Boolean
) {
    val state by remember { viewModel.uiState }
    val currentQuestionIndex by remember { viewModel.currentQuestionIndex }
    val nextButtonIsActive by remember { viewModel.enabledNextButton }

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
    viewModel: Sf36ViewModel
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
            is TestQuestion.PreQuestion -> {
                Text(
                    text = question.question,
                    fontSize = 18.sp,
                    color = Color(0xFF1C1B1F)
                )
            }
            is TestQuestion.SingleAnswer -> {
                SingleAnswersVariant(question, isSending) {
                    viewModel.selectAnswerInSingleAnswer(it)
                }
            }
            is TestQuestion.YesNo -> { YesNoVariant(question, viewModel) }
            else -> {}
        }
    }
}

@Composable
fun SingleAnswersVariant(
    question: TestQuestion.SingleAnswer,
    isSending: Boolean,
    selectAnswer: (Pair<String, Int>) -> Unit
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    if (question.answers.isNotEmpty()) {
        question.answers.forEach { answer ->
            val isSelected = question.selectedAnswer == answer
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .background(
                        color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        if (!isSending) {
                            selectAnswer(answer)
                        }
                    }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(text = answer.first, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun YesNoVariant(
    question: TestQuestion.YesNo,
    viewModel: Sf36ViewModel
) {
    Text(
        text = question.question,
        fontSize = 17.sp,
        color = Color(0xFF1C1B1F)
    )
    Spacer(modifier = Modifier.height(24.dp))
    question.answers.forEach { answer ->
        Text(
            text = answer.question,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnswersVariants(answer) { viewModel.selectAnswerInYesNoAnswer(answer.question, it) }
        Spacer(modifier = Modifier.height(24.dp))
    }
}