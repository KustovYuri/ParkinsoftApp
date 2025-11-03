package com.farma.parkinsoftapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.presentation.navigation.AppNavHost
import com.farma.parkinsoftapp.ui.theme.ParkinsoftAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val activityViewModel: MainActivityViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepVisibleCondition {
                activityViewModel.isLoading.value
            }
        }

        setContent {
            val userRole by activityViewModel.userRole.collectAsState()

            ParkinsoftAppTheme {
                if (userRole != null) {
                    AppNavHost(
                        userRole = userRole ?: UserRoleValues.UNAUTHORIZED,
                    )
                }
            }
        }
    }
}