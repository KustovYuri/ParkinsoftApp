package com.farma.parkinsoftapp.presentation.doctor.patient_current_test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.data.network.models.TestResultModel
import com.farma.parkinsoftapp.presentation.common.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientCurrentTestScreen(
    viewModel: PatientCurrentTestScreenViewModel = hiltViewModel<PatientCurrentTestScreenViewModel>(),
    backNavigation: () -> Boolean,
    patientInitials: String,
    testDate: String
) {
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopScreenBar(backNavigation = backNavigation, patientInitials) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            when(uiState) {
                is ScreenState.Error -> {
                    Text((uiState as ScreenState.Error).message)
                }

                is ScreenState.Loading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF178399)
                    )
                }
                is ScreenState.Success -> {
                    Screen(paddingValues, testDate, (uiState as ScreenState.Success).data)
                }
            }
        }
    }
}

@Composable
private fun Screen(
    paddingValues: PaddingValues,
    testDate: String,
    testAnswers: List<TestResultModel>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Опросы",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        TestDate(testDate)
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(testAnswers) { testAnswer ->
                Spacer(Modifier.height(24.dp))
                TestItem(testAnswer)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TestItem(testAnswer: TestResultModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 275.dp)
        ) {
            Text(
                text = testAnswer.testQuestion,
                fontSize = 13.sp,
                color = Color(0xFF002A33),
                style = TextStyle(
                    lineHeight = 16.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = testAnswer.testAnswer,
                fontSize = 17.sp,
                color = Color(0xFF002A33)
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "${testAnswer.testScore} / ${testAnswer.testMaxScope}",
            fontSize = 17.sp,
            color = if (testAnswer.testScore > testAnswer.testMaxScope / 2) {
                Color(0xFF459C62)
            } else {
                Color(0xFFD21010)
            }
        )
    }
}

@Composable
private fun TestDate(testDate: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.clock),
            contentDescription = null,
            tint = Color(0xFF62767A)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = testDate,
            color = Color(0xFF62767A),
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(backNavigation: () -> Boolean, patientInitials: String) {
    TopAppBar(
        title = {
            Text(
                text = patientInitials,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = { backNavigation() }) {
                Icon(
                    painterResource(R.drawable.arrow_left),
                    contentDescription = "Назад",
                    tint = Color(0xFF002A33)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO сортировка */ }) {
                Icon(
                    painter = painterResource(R.drawable.printer),
                    contentDescription = "Печать",
                    tint = Color(0xFF002A33)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}