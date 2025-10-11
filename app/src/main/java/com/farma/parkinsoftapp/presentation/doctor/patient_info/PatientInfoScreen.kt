package com.farma.parkinsoftapp.presentation.doctor.patient_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.presentation.doctor.all_patients.Patient

private enum class TestsTabs{
    DAILY, CONTROL
}

@Composable
fun PatientInfoScreen() {
    var selectedTab by remember { mutableStateOf(TestsTabs.DAILY) }
    var selectedTestChip by remember { mutableStateOf("Дневник тестовой стимуляции") }

    Scaffold(
        topBar = { TopScreenBar { } }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(34.dp))
            PatientItem()
            Spacer(Modifier.height(24.dp))
            TherapyDate()
            Spacer(Modifier.height(12.dp))
            TherapyResult()
            Spacer(Modifier.height(28.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Опросы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Tabs(
                selectedTab = selectedTab,
                clickTab = { tab: TestsTabs ->
                    selectedTestChip = ""
                    selectedTab = tab
                }
            )
            Spacer(Modifier.height(12.dp))
            when(selectedTab) {
                TestsTabs.DAILY -> {
                    TestChips(
                        tests = listOf(
                            "Дневник тестовой стимуляции",
                            "Дневник общего самочувствия"
                        ),
                        selectedTestChip = selectedTestChip,
                        onChipSelected = { selectedChip ->
                            selectedTestChip = selectedChip
                        }
                    )
                    Spacer(Modifier.height(28.dp))
                    (1..15).forEach { _ ->
                        TestItem()
                    }
                }
                TestsTabs.CONTROL -> {
                    TestChips(
                        tests = listOf("HADS", "DN4", "Освестри", "SF-36", "LANSS", "PainDetect"),
                        selectedTestChip = selectedTestChip,
                        onChipSelected = { selectedChip ->
                            selectedTestChip = selectedChip
                        }
                    )
                    Spacer(Modifier.height(28.dp))
                    (1..15).forEach { _ ->
                        TestItem()
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TestItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                text = "Сегодня, 23:56",
                color = Color(0xFF62767A),
                fontSize = 13.sp
            )
            Text(
                "Опросник",
                fontSize = 15.sp,
                color = Color(0xFF002A33)
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.icon__4_),
            contentDescription = null,
            tint = Color(0xFF459C62)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "27 / 60",
            color = Color(0xFF459C62),
            fontSize = 17.sp
        )
        Spacer(Modifier.width(12.dp))
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                painter = painterResource(R.drawable.chevron_right),
                contentDescription = null,
                tint = Color.Gray
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF178399))
            )
        }
    }
}

@Composable
private fun TestChips(
    tests: List<String>,
    selectedTestChip: String,
    onChipSelected: (selectedChip: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.width(12.dp))
        DailyTestsChips(
            tests,
            selectedTestChip,
            onChipSelected
        )
        Spacer(Modifier.width(20.dp))
    }
}

@Composable
private fun DailyTestsChips(
    dailyTest: List<String>,
    selectedTestChip: String,
    onChipSelected: (String) -> Unit,
) {

    dailyTest.forEach { test ->
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .height(37.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(
                    color = if (selectedTestChip == test) {
                        Color(0xFFA9E0EB)
                    } else {
                        Color(0xFFEDF1F2)
                    },
                )
                .clickable { onChipSelected(test) }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = test, fontSize = 13.sp)
        }
    }
}

@Composable
private fun Tabs(selectedTab: TestsTabs, clickTab: (TestsTabs) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                color = Color(0xFFEDF1F2),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
    ) {
        TabButton(
            modifier = Modifier.weight(1f),
            text = "Ежедневные",
            selected = selectedTab == TestsTabs.DAILY,
            onClick = { clickTab(TestsTabs.DAILY) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        TabButton(
            modifier = Modifier.weight(1f),
            text = "Контрольные",
            selected = selectedTab == TestsTabs.CONTROL,
            onClick = { clickTab(TestsTabs.CONTROL) },
        )
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val background = if (selected) Color(0xFF178399) else Color(0xFFEDF1F2)
    val contentColor = if (selected) Color(0xFFFFFFFF) else Color(0xFF002A33)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp
        )
    }
}

@Composable
private fun TherapyResult() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon__4_),
            contentDescription = null,
            tint = Color(0xFF459C62)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Идет на поправку",
            color = Color(0xFF459C62),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun TherapyDate() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.clock),
            contentDescription = null,
            tint = Color(0xFF62767A)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "На лечении с 23.12.2023",
            color = Color(0xFF62767A),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun PatientItem(
    patient: Patient = Patient(
        id = 1,
        firstName = "Любовь",
        lastName = "Константинопольская",
        middleName = "Сергеевна",
        age = 63,
        diagnosis = "Заболевание",
        onTreatment = false,
        unreadTests = 0,
        sex = false
    )
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватар с инициалами
        Box(
            modifier = Modifier
                .size(60.dp)
//                .padding(top = 4.dp)
                .background(
                    color = if (patient.sex) Color(0xFFE1E7FA) else Color(0xFFFAE1E9),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = patient.initials,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF002A33)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column() {
            Row {
                Text(
                    "${patient.age} года",
                    fontSize = 15.sp,
                    color = Color(0xFF002A33)
                )
                Text(
                    " · ${patient.diagnosis}",
                    fontSize = 15.sp,
                    color = Color(0xFF62767A)
                )
            }
            Text(
                modifier = Modifier
                    .height(20.dp),
                text = "${patient.lastName}",
                fontSize = 15.sp,
                color = Color(0xFF002A33)
            )
            Text(
                modifier = Modifier
                    .height(20.dp),
                text = "${patient.firstName} ${patient.middleName}",
                fontSize = 15.sp,
                color = Color(0xFF002A33),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(onClose: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    painterResource(R.drawable.arrow_left),
                    contentDescription = "Закрыть",
                    tint = Color(0xFF002A33)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO сортировка */ }) {
                Icon(
                    painter = painterResource(R.drawable.ellipsis_vertical),
                    contentDescription = "Дополнительно",
                    tint = Color(0xFF002A33)
                )
            }
        },
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}