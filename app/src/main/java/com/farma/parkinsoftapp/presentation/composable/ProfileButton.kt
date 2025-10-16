package com.farma.parkinsoftapp.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.farma.parkinsoftapp.R

@Composable
fun ProfileButton(
    logOut: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box() {
        IconButton(onClick = {expanded.value = !expanded.value}) {
            Icon(
                painterResource(R.drawable.user),
                contentDescription = "Профиль",
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
                        text = "Выход",
                    )
                },
                onClick = { logOut() },
            )
        }
    }
}