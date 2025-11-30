package com.farma.parkinsoftapp.presentation.patient.all_tests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.data.network.models.ShortPatient
import com.farma.parkinsoftapp.domain.models.patient.PatientTestPreview
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.utils.convertStringDateToLocalDateTime
import com.farma.parkinsoftapp.presentation.composable.ProfileButton
import com.farma.parkinsoftapp.presentation.doctor.patient_info.formatRu
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAllTestsScreen(
    viewModel: PatientAllTestsScreenViewModel = hiltViewModel<PatientAllTestsScreenViewModel>(),
    navigateToLogin: () -> Unit,
    navigateToTest: (Long, TestType) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var previousDaysIsOver by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val shortPatient by viewModel.shortPatient.collectAsState()

    Scaffold(
        topBar = {
            TopBar {
                scope.launch {
                    viewModel.logOut()
                    navigateToLogin()
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
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
            } else if (shortPatient?.isDischarge ?: false) {
                Image(
                    painter = painterResource(R.drawable.final_health),
                    contentDescription = null
                )
            } else {
                TestPreviewList(state, navigateToTest, previousDaysIsOver, viewModel, shortPatient)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Discharge(dischargeDateTime: String) {
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
            text = "Выписка назначена\nна ${convertStringDateToLocalDateTime(dischargeDateTime).formatRu()}",
            fontSize = 15.sp,
            style = TextStyle(
                lineHeight = 16.sp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TestPreviewList(
    state: AllPreviewTestsState,
    navigateToTest: (Long, TestType) -> Unit,
    previousDaysIsOver: Boolean,
    viewModel: PatientAllTestsScreenViewModel,
    shortPatient: ShortPatient?
) {
    var previousDaysIsOver1 = previousDaysIsOver
    Spacer(modifier = Modifier.height(20.dp))
    if (shortPatient != null && shortPatient.dischargeDate != null) {
        Discharge(shortPatient.dischargeDate)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {

        for ((day, listTestsPreview) in state.testsPreviewByDays) {
            if (day == LocalDate.now()) {
                item {
                    SectionHeader("За сегодня", Modifier.padding(top = 12.dp, bottom = 24.dp))
                }
                items(listTestsPreview) { test ->
                    TestItem(test) {
                        navigateToTest(test.id, test.testType)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                if (!previousDaysIsOver1) {
                    item {
                        SectionHeader(
                            "За пропущенные дни",
                            Modifier.padding(top = 44.dp, bottom = 16.dp)
                        )
                    }
                    previousDaysIsOver1 = true
                }
                item {
                    DateLabel(viewModel.convertLocalDateToUiDate(day))
                    Spacer(modifier = Modifier.height(24.dp))
                }
                items(listTestsPreview) { test ->
                    TestItem(test) {
                        navigateToTest(test.id, test.testType)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(logOut: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Опросники",
                fontSize = 24.sp,
                color = Color(0xFF002A33),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            ProfileButton {
                logOut()
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.history),
                    contentDescription = "История",
                    tint = Color(0xFF002A33)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 24.sp,
        color = Color(0xFF002A33),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun DateLabel(label: String) {
    Text(
        text = label,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF002A33),
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

@Composable
fun TestItem(test: PatientTestPreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${test.questionCount} вопросов  •  ${test.testTime} мин",
                color = Color(0xFF62767A),
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = test.testName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF002A33)
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatusChip(test.isSuccessTest)
        }

        Icon(
            painter = painterResource(R.drawable.chevron_right),
            contentDescription = null,
            tint = Color(0xFF62767A),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun StatusChip(status: Boolean) {
    val statusChipContent = when (status) {
        false -> StatusChipContent(
            "Нужно заполнить",
            Color(0xFFA9E0EB),
            Color(0xFF002A33),
            R.drawable.edit_icon
        )

        true -> StatusChipContent(
            "Заполнен",
            Color(0xFF459C62),
            Color(0xFFFFFFFF),
            R.drawable.edited_icon
        )
    }

    Row(
        modifier = Modifier
            .background(statusChipContent.bgColor, shape = MaterialTheme.shapes.large)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(statusChipContent.icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = statusChipContent.textColor
        )
        Spacer(Modifier.width(4.dp))
        Text(text = statusChipContent.text, fontSize = 13.sp, color = statusChipContent.textColor)
    }
}

data class StatusChipContent(
    val text: String,
    val bgColor: Color,
    val textColor: Color,
    val icon: Int
)