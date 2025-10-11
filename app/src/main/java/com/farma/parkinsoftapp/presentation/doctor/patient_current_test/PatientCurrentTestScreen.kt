package com.farma.parkinsoftapp.presentation.doctor.patient_current_test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R

data class TestAnswer(
    val testQuestion: String,
    val testAnswer: String,
    val testScores: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientCurrentTestScreen() {
    val testAnswers = listOf(
        TestAnswer(
            testQuestion = "Самообслуживание\n(умывание, одевание и т.д.)",
            testAnswer = "Я могу обслужить себя нормально без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Стояние",
            testAnswer = "Я могу стоять столько, сколько захочу, без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Сидение",
            testAnswer = "Я могу сидеть на любом стуле столько, сколько захочу",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Самообслуживание\n(умывание, одевание и т.д.)",
            testAnswer = "Я могу обслужить себя нормально без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Стояние",
            testAnswer = "Я могу стоять столько, сколько захочу, без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Сидение",
            testAnswer = "Я могу сидеть на любом стуле столько, сколько захочу",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Самообслуживание\n(умывание, одевание и т.д.)",
            testAnswer = "Я могу обслужить себя нормально без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Стояние",
            testAnswer = "Я могу стоять столько, сколько захочу, без особой боли",
            testScores = "1 / 5",
        ),
        TestAnswer(
            testQuestion = "Сидение",
            testAnswer = "Я могу сидеть на любом стуле столько, сколько захочу",
            testScores = "1 / 5",
        ),
    )

    Scaffold(
        topBar = { TopScreenBar() }
    ) { paddingValues ->
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
            TestDate()
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(testAnswers) { testAnswer->
                    Spacer(Modifier.height(24.dp))
                    TestItem(testAnswer)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TestItem(testAnswer: TestAnswer) {
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
            text = testAnswer.testScores,
            fontSize = 17.sp,
            color = Color(0xFF459C62)
        )
    }
}

@Composable
private fun TestDate() {
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
            text = "23 января 23:45",
            color = Color(0xFF62767A),
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar() {
    TopAppBar(
        title = {
            Text(
                text = "Константинопольский К. А.",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
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