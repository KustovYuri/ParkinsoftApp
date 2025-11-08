package com.farma.parkinsoftapp.presentation.doctor.all_patients

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.domain.models.patient.PatientModel
import com.farma.parkinsoftapp.presentation.composable.ProfileButton
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPatientsScreen(
    viewModel: AllPatientsViewModel = hiltViewModel<AllPatientsViewModel>(),
    navigateToAddNewPatientScreen: (Long) -> Unit,
    navigateToPatient: (Long) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val isRefreshing by remember { viewModel.isRefreshing }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.getData()
        },
    ) {
        Scaffold(
            containerColor = Color(0xFFFFFFFF),
            topBar = {
                TopScreenBar {
                    scope.launch {
                        viewModel.logOut()
                        navigateToLogin()
                    }
                }
            },
            floatingActionButton = {
                AddNewPatientFloatingButton {
                    navigateToAddNewPatientScreen(
                        uiState.doctorId
                    )
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Поисковая строка
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = {
                        Text(
                            "Искать пациента",
                            color = Color(0xFF62767A)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "Поиск пациента",
                            tint = Color(0xFF62767A)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFEDF1F2),
                        unfocusedContainerColor = Color(0xFFEDF1F2),
                        unfocusedIndicatorColor = Color(0xFFEDF1F2),
                        focusedIndicatorColor = Color(0xFF62767A),
                        focusedTextColor = Color(0xFF002A33)
                    )
                )

                // Переключатель вкладок
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEDF1F2),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp)
                ) {
                    TabButton(
                        modifier = Modifier.weight(1f),
                        text = "На лечении",
                        selected = uiState.selectedTab == PatientsTab.OnTreatment,
                        onClick = { viewModel.onTabSelected(PatientsTab.OnTreatment) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TabButton(
                        modifier = Modifier.weight(1f),
                        text = "Выписаны",
                        selected = uiState.selectedTab == PatientsTab.Discharged,
                        onClick = { viewModel.onTabSelected(PatientsTab.Discharged) },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Список пациентов
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredPatients) { patient ->
                        PatientItem(
                            patient = patient,
                            click = { navigateToPatient(patient.id ?: -1) },
                            calculateAge = { birthdate: String ->
                                viewModel.calculateAge(birthdate)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddNewPatientFloatingButton(navigateToAddNewPatientScreen: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.size(64.dp),
        shape = CircleShape,
        onClick = {
            navigateToAddNewPatientScreen()
        },
        containerColor = Color(0xFF00838F)
    ) {
        Icon(
            painter = painterResource(R.drawable.icon__2_),
            contentDescription = "Добавить пациента",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(logOut: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Мои пациенты",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            ProfileButton {
                logOut()
            }
        },
        actions = {
            IconButton(onClick = { /* TODO сортировка */ }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_down_up),
                    contentDescription = "Сортировка",
                    tint = Color(0xFF002A33)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val background = if (selected) Color(0xFF178399) else Color(0xFFEDF1F2)
    val contentColor = if (selected) Color(0xFFFFFFFF) else Color(0xFF002A33)

    Box(
        modifier = modifier
            .background(background, shape = RoundedCornerShape(8.dp))
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
private fun PatientItem(patient: PatientModel, calculateAge: (String) -> Int, click: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable {
                click()
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватар с инициалами
        Box(
            modifier = Modifier
                .size(48.dp)
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

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    "${calculateAge(patient.birthDate)} года",
                    fontSize = 15.sp,
                    color = Color(0xFF002A33)
                )
                Text(
                    " · ",
                    fontSize = 15.sp,
                    color = Color(0xFF62767A)
                )
                Text(
                    patient.diagnosis,
                    fontSize = 13.sp,
                    color = Color(0xFF62767A)
                )
            }
            Text(
                patient.fullName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF002A33)
            )
        }

        if (patient.unreadTests > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00838F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = patient.unreadTests.toString(),
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            painter = painterResource(R.drawable.chevron_right),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
