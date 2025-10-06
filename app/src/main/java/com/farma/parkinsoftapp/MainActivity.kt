package com.farma.parkinsoftapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.farma.parkinsoftapp.presentation.navigation.AppNavHost
import com.farma.parkinsoftapp.presentation.patient.test.PatientTestScreen
import com.farma.parkinsoftapp.ui.theme.ParkinsoftAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ParkinsoftAppTheme {
                AppNavHost()
            }
        }
    }
}