package com.farma.parkinsoftapp.presentation.patient.test.composable_common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.domain.models.patient.TestType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreenBar(closeTest: () -> Boolean, testType: TestType) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Text(
                text = when(testType){
                    TestType.TEST_STIMULATION_DIARY -> "Дневник тестовой стимуляции"
                    TestType.HADS1 -> "HADS 1"
                    TestType.HADS2 -> "HADS 2"
                    TestType.OSVESTRY -> "Освестри"
                    TestType.LANSS -> "LANSS"
                    TestType.DN4 -> "DN4"
                    TestType.SF36 -> "SF36"
                    TestType.PAIN_DETECTED -> "Pain Detected"
                },
                fontSize = 17.sp,
                color = Color(0xFF002A33)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                closeTest()
            }) {
                Icon(painterResource(R.drawable.x), contentDescription = "Закрыть")
            }
        }
    )
}