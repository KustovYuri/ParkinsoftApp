package com.farma.parkinsoftapp.presentation.doctor.new_patient_tests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPatientsTestScreen(
    viewModel: NewPatientsTestViewModel = hiltViewModel<NewPatientsTestViewModel>(),
    backNavigation: () -> Unit,
    nextScreenNavigation: () -> Unit
) {
    val controlTests = viewModel.controlTests
    val dailyTests = viewModel.dailyTests
    val selectedControlItems by remember { viewModel.selectedControlItems }
    val selectedDailyItems by remember { viewModel.selectedDailyItems }

    Scaffold(
        topBar = { TopScreenBar(backNavigation) },
        containerColor = Color(0xFFFFFFFF)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(34.dp))
            Text(
                text = "Контрольные опросники",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "При постановке на лечение и при выписке",
                fontSize = 17.sp,
            )
            Spacer(Modifier.height(24.dp))
            ControlTestsChips(controlTests, selectedControlItems) {item: String, isSelected: Boolean ->
                viewModel.setSelectedControlItem(item, isSelected)
            }
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Ежедневные опросники",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Для пациента",
                fontSize = 17.sp,
            )
            Spacer(Modifier.height(24.dp))
            DailyTestsChips(dailyTests, selectedDailyItems) {item: String, isSelected: Boolean ->
                viewModel.setSelectedDailyItem(item, isSelected)
            }
            Spacer(Modifier.weight(1f))
            NextButton(
                isActive = true,
                click = {
                    nextScreenNavigation()
                }
            )
            Spacer(Modifier.height(35.dp))
        }

    }
}

@Composable
private fun DailyTestsChips(
    dailyTest: List<String>,
    selectedDailyItems: Set<String>,
    itemClick: (String, Boolean)-> Unit
) {
    dailyTest.forEach { dailyTest ->
        val isSelected = selectedDailyItems.contains(dailyTest)

        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(
                    color = if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                )
                .clickable { itemClick(dailyTest, isSelected) }
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(text = dailyTest, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ControlTestsChips(
    controlItems: List<String>,
    selectedControlItems: Set<String>,
    itemClick: (String, Boolean)-> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        controlItems.forEach { controlTest ->
            val isSelected = selectedControlItems.contains(controlTest)

            Text(
                text = controlTest,
                color = Color(0xFF002A33),
                fontSize = 15.sp,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(100))
                    .background(
                        if (isSelected) Color(0xFFA9E0EB) else Color(0xFFEDF1F2),
                    )
                    .clickable { itemClick(controlTest, isSelected) }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(onClose: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Выбор опросника",
                fontSize = 17.sp,
                color = Color(0xFF002A33),
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(painterResource(R.drawable.arrow_left), contentDescription = "Закрыть")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}

@Composable
private fun NextButton(
    isActive: Boolean,
    click: () -> Unit,
) {
    TextButton(
        enabled = isActive,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = click,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF178399),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFFEDF1F2),
            disabledContentColor = Color(0xFFB2BFC2)
        )
    ) {
        Text(
            text = "Продолжить",
            fontSize = 17.sp,
            fontWeight = FontWeight(400),
        )
    }
}
