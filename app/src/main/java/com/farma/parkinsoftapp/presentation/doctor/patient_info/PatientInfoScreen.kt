package com.farma.parkinsoftapp.presentation.doctor.patient_info

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.data.network.models.LargePatientModel
import com.farma.parkinsoftapp.data.network.models.TestPreviewModel
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.models.patient.AllTestsTypes
import com.farma.parkinsoftapp.domain.utils.convertStringDateToLocalDateTime
import com.farma.parkinsoftapp.presentation.common.ScreenState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private enum class TestsTabs {
    DAILY, CONTROL
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientInfoScreen(
    viewModel: PatientInfoViewModel = hiltViewModel<PatientInfoViewModel>(),
    backNavigation: () -> Unit,
    navigateToTestInfo: (String, String, TestType, Long, Boolean) -> Unit
) {
    val selectedTab = remember { mutableStateOf(TestsTabs.DAILY) }
    val selectedTestChip = remember { mutableStateOf(AllTestsTypes.TEST_STIMULATION_DIARY) }
    val patientState by viewModel.patientState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getPatientInfo()
    }

    Scaffold(
        topBar = {
            TopScreenBar(
                context,
                { viewModel.changeDischargeDateTime(it) }
            ) { backNavigation() }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            when (patientState) {
                is ScreenState.Error -> {
                    Text((patientState as ScreenState.Error).message)
                }

                is ScreenState.Loading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF178399)
                    )
                }

                is ScreenState.Success -> {
                    Screen(
                        viewModel,
                        (patientState as ScreenState.Success<LargePatientModel>).data,
                        paddingValues,
                        selectedTab,
                        selectedTestChip,
                        navigateToTestInfo,
                        calculateAge = { date: String ->
                            viewModel.calculateAge(date)
                        },
                        { viewModel.changeDischargeDateTime(it) }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Screen(
    viewModel: PatientInfoViewModel,
    patientInfo: LargePatientModel,
    paddingValues: PaddingValues,
    selectedTab: MutableState<TestsTabs>,
    selectedTestChip: MutableState<AllTestsTypes>,
    navigateToTestInfo: (String, String, TestType, Long, Boolean) -> Unit,
    calculateAge: (String) -> Int,
    changeDischargeDateTime: (LocalDateTime?) -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(34.dp))
            PatientItem(patientInfo, calculateAge)
            Spacer(Modifier.height(24.dp))
            if (!patientInfo.isDischarge) {
                TherapyDate(patientInfo)
            }
            Spacer(Modifier.height(12.dp))
            if (patientInfo.dateDischarge != null) {
                if (!patientInfo.isDischarge) {
                    Discharge(
                        convertStringDateToLocalDateTime(patientInfo.dateDischarge),
                        changeDischargeDateTime
                    )
                } else {
                    DischargeTrue(
                        patientInfo,
                        convertStringDateToLocalDateTime(patientInfo.dateDischarge)
                    )
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Опросы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Tabs(
                selectedTab = selectedTab.value,
                clickTab = { tab: TestsTabs ->
                    selectedTestChip.value =
                        if (tab == TestsTabs.DAILY) {
                            AllTestsTypes.TEST_STIMULATION_DIARY
                        } else {
                            AllTestsTypes.HADS1
                        }
                    selectedTab.value = tab
                }
            )
            Spacer(Modifier.height(12.dp))
            when (selectedTab.value) {
                TestsTabs.DAILY -> {
                    selectedTestChip.value = AllTestsTypes.TEST_STIMULATION_DIARY
                    patientInfo.testsPreview
                        .filter {
                            it.testType == selectedTestChip.value.testType
                        }.forEach {
                            TestItem(
                                secondNameWithInitials = patientInfo.secondNameWithInitials,
                                testPreviewInfo = it,
                                click = navigateToTestInfo
                            )
                        }
                }

                TestsTabs.CONTROL -> {
                    TestChips(
                        tests = listOf(
                            AllTestsTypes.HADS1,
                            AllTestsTypes.HADS2,
                            AllTestsTypes.DN4,
                            AllTestsTypes.OSVESTRY,
                            AllTestsTypes.SF36,
                            AllTestsTypes.LANSS,
                            AllTestsTypes.PAIN_DETECTED
                        ),
                        selectedTestChip = selectedTestChip.value,
                        onChipSelected = { selectedChip ->
                            selectedTestChip.value = selectedChip
                        }
                    )
                    Spacer(Modifier.height(28.dp))
                    patientInfo.testsPreview
                        .filter {
                            it.testType == selectedTestChip.value.testType
                        }.forEach {
                            TestItem(
                                secondNameWithInitials = patientInfo.secondNameWithInitials,
                                testPreviewInfo = it,
                                click = navigateToTestInfo,
                            )
                        }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        if (patientInfo.dateDischarge != null) {
            val targetDateTime: LocalDateTime =
                convertStringDateToLocalDateTime(patientInfo.dateDischarge)
            val targetDate = targetDateTime.toLocalDate()

            val today = LocalDate.now()

            val daysLeft = ChronoUnit.DAYS.between(today, targetDate)

            if (!patientInfo.isDischarge) {
                if (patientInfo.allControlTestsIsComplete && patientInfo.finalTestsIsSending) {
                    NextButton(
                        text = "Выписать пациента",
                        isActive = true,
                        isLoading = false,
                        click = { viewModel.dischargePatient() }
                    )
                } else if (daysLeft <= 1L && !patientInfo.finalTestsIsSending) {
                    NextButton(
                        text = "Провести осмотр",
                        isActive = true,
                        isLoading = false,
                        click = { viewModel.createFinalTests() }
                    )
                }
            }
        }
    }
}

@Composable
private fun NextButton(
    isActive: Boolean,
    isLoading: Boolean,
    click: () -> Unit,
    text: String,
) {
    TextButton(
        enabled = isActive,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(56.dp),
        onClick = click,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF178399),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFFE1F2F5),
            disabledContentColor = Color(0xFFB2BFC2)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFFFFFFFF)
            )
        } else {
            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight(400),
            )
        }
    }
}

private fun isNativeTest(testType: TestType): Boolean {
    return when (testType) {
        TestType.TEST_STIMULATION_DIARY -> true
        TestType.HADS1 -> false
        TestType.HADS2 -> false
        TestType.OSVESTRY -> false
        TestType.LANSS -> false
        TestType.DN4 -> true
        TestType.SF36 -> true
        TestType.PAIN_DETECTED -> true
    }
}

@Composable
private fun TestItem(
    secondNameWithInitials: String,
    testPreviewInfo: TestPreviewModel,
    click: (String, String, TestType, Long, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable {
                val testType = TestType.fromString(testPreviewInfo.testType)
                    ?: TestType.TEST_STIMULATION_DIARY
                click(
                    secondNameWithInitials,
                    testPreviewInfo.testDate,
                    testType,
                    testPreviewInfo.id ?: -1,
                    isNativeTest(testType)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                text = testPreviewInfo.testCompletedDate,
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
        if (testPreviewInfo.progressStatus) {
            Icon(
                painter = painterResource(R.drawable.icon__4_),
                contentDescription = null,
                tint = Color(0xFF459C62)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.icon__5_),
                contentDescription = null,
                tint = Color(0xFFE27878)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            modifier = Modifier.width(60.dp),
            text = "${testPreviewInfo.summaryPoints}/${testPreviewInfo.maxPoints}",
            color = if (testPreviewInfo.progressStatus) {
                Color(0xFF459C62)
            } else {
                Color(0xFFE27878)
            },
            fontSize = 17.sp,
            textAlign = TextAlign.End
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
            if (!(testPreviewInfo.isViewed ?: true)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF178399))
                )
            }
        }
    }
}

@Composable
private fun TestChips(
    tests: List<AllTestsTypes>,
    selectedTestChip: AllTestsTypes,
    onChipSelected: (AllTestsTypes) -> Unit
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
    dailyTest: List<AllTestsTypes>,
    selectedTestChip: AllTestsTypes,
    onChipSelected: (AllTestsTypes) -> Unit,
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
            Text(text = test.testName, fontSize = 13.sp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Discharge(
    dischargeDateTime: LocalDateTime,
    changeDischargeDateTime: (LocalDateTime?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
            .background(
                color = Color(0xFFEDF1F2),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(R.drawable.calendar),
            contentDescription = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Выписка назначена\nна ${dischargeDateTime.formatRu()}",
            fontSize = 15.sp,
            style = TextStyle(
                lineHeight = 16.sp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd // важное место!
        ) {

            IconButton(onClick = { expanded = true }) {
                Icon(
                    painter = painterResource(R.drawable.ellipsis_vertical),
                    contentDescription = "Дополнительно",
                    tint = Color(0xFF002A33)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(160.dp)
                    .background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Изменить") },
                    onClick = { showDatePicker = true }
                )
                DropdownMenuItem(
                    text = { Text("Отменить выписку") },
                    onClick = { changeDischargeDateTime(null) }
                )
            }
            ShowDateTimePicker(
                showDatePicker,
                { showDatePicker = !showDatePicker }) { changeDischargeDateTime(it) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DischargeTrue(patient: LargePatientModel, dischargeDateTime: LocalDateTime) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
            .background(
                color = Color(0xFFEDF1F2),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(R.drawable.calendar),
            contentDescription = null,
            tint = Color(0xFF62767A)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Был(а) на лечении\nc ${patient.dateReceipt} до ${
                dischargeDateTime.toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            } ",
            fontSize = 15.sp,
            style = TextStyle(
                lineHeight = 16.sp
            ),
            color = Color(0xFF62767A)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatRu(): String {
    val formatter = DateTimeFormatter.ofPattern("EEE d MMMM HH:mm", Locale("ru"))
    return this.format(formatter)
        .replaceFirstChar { it.uppercase() } // Делаем "Пн" вместо "пн"
}

@Composable
private fun TherapyDate(patientInfo: LargePatientModel) {
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
            text = "На лечении с ${patientInfo.dateReceipt}",
            color = Color(0xFF62767A),
            fontSize = 15.sp
        )
    }
}

@Composable
private fun PatientItem(
    patientInfo: LargePatientModel,
    calculateAge: (String) -> Int
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
                    color = if (patientInfo.sex) Color(0xFFE1E7FA) else Color(0xFFFAE1E9),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = patientInfo.initials,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF002A33)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column() {
            Row {
                Text(
                    "${calculateAge(patientInfo.birthDate)} года",
                    fontSize = 15.sp,
                    color = Color(0xFF002A33)
                )
                Text(
                    " · ${patientInfo.diagnosis}",
                    fontSize = 15.sp,
                    color = Color(0xFF62767A)
                )
            }
            Text(
                modifier = Modifier
                    .height(20.dp),
                text = patientInfo.secondName,
                fontSize = 15.sp,
                color = Color(0xFF002A33)
            )
            Text(
                modifier = Modifier
                    .height(20.dp),
                text = "${patientInfo.name} ${patientInfo.middleName}",
                fontSize = 15.sp,
                color = Color(0xFF002A33),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(
    context: Context,
    changeDischargeDateTime: (LocalDateTime) -> Unit,
    onClose: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

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
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(
                    painter = painterResource(R.drawable.ellipsis_vertical),
                    contentDescription = "Дополнительно",
                    tint = Color(0xFF002A33)
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .width(160.dp)
                    .background(Color.White),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = {
                        Text(
                            text = "Назначить выписку",
                        )
                    },
                    onClick = {
                        showDatePicker = true
                    },
                )
            }
            ShowDateTimePicker(
                showDatePicker,
                { showDatePicker = !showDatePicker }) { changeDischargeDateTime(it) }

        },
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ShowDateTimePicker(
    showDatePicker: Boolean,
    changeShowDatePicker: () -> Unit,
    changeDischargeDateTime: (LocalDateTime) -> Unit,
) {
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { changeShowDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        changeShowDatePicker()
                        showTimePicker =
                            true      // ➜ после выбора даты запускаем TimePicker
                    }
                }) { Text("Далее") }
            },
            dismissButton = {
                TextButton(onClick = { changeShowDatePicker() }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // ---------- TIME PICKER ----------
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(
                        timePickerState.hour,
                        timePickerState.minute
                    )

                    showTimePicker = false

                    // Когда выбраны и дата, и время -> возвращаем итог
                    if (selectedDate != null && selectedTime != null) {
                        changeDischargeDateTime(LocalDateTime.of(selectedDate, selectedTime))
                    }

                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Отмена")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
