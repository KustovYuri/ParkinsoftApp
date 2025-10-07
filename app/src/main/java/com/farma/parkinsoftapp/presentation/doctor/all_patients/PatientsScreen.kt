package com.farma.parkinsoftapp.presentation.doctor.all_patients

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(viewModel: PatientsViewModel = hiltViewModel<PatientsViewModel>()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои пациенты") },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Переход в профиль", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            painterResource(R.drawable.user),
                            contentDescription = "Профиль"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO сортировка */ }) {
                        Icon(painter = painterResource(R.drawable.user), contentDescription = "Сортировка")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "Добавить пациента", Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF00838F)
            ) {
                Icon(painter = painterResource(R.drawable.x), contentDescription = "Добавить пациента", tint = Color.White)
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
                placeholder = { Text("Искать пациента") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Переключатель вкладок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
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
                    PatientItem(patient = patient)
                }
            }
        }
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val background = if (selected) Color(0xFF00838F) else Color.White
    val contentColor = if (selected) Color.White else Color.Black

    Box(
        modifier = modifier
            .background(background, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = contentColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PatientItem(patient: Patient) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable {
                Toast.makeText(context, "Открыт пациент: ${patient.fullName}", Toast.LENGTH_SHORT).show()
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватар с инициалами
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (patient.lastName.first().isUpperCase()) Color(0xFFFFCDD2) else Color(0xFFBBDEFB),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = patient.initials,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("${patient.age} года · ${patient.disease}", fontSize = 14.sp, color = Color.Gray)
            Text(patient.fullName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        if (patient.unreadTests > 0) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF00838F), shape = CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = patient.unreadTests.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
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
