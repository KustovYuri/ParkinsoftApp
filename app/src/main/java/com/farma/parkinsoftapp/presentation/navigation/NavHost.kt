package com.farma.parkinsoftapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.presentation.login.login_screen.LoginScreen
import com.farma.parkinsoftapp.presentation.login.sms_screen.SmsScreen
import com.farma.parkinsoftapp.presentation.patient.all_tests.PatientAllTestsScreen


@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = PatientAllTestsRoute) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToSms = { phoneNumber: String ->
                    navController.navigate(SmsRoute(phoneNumber))
                }
            )
        }
        composable<SmsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<SmsRoute>()
            SmsScreen(
                phoneNumber = args.phoneNumber,
                backNavigation = {
                    navController.popBackStack()
                },
                forwardNavigation = {
                    navController.navigate(PatientAllTestsRoute)
                }
            )
        }
        composable<PatientAllTestsRoute> {
            PatientAllTestsScreen()
        }
    }
}

