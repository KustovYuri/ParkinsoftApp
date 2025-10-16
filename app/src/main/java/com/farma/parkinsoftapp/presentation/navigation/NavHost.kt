package com.farma.parkinsoftapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.farma.parkinsoftapp.data.local.data_store.UserRoleValues
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.presentation.doctor.all_patients.AllPatientsScreen
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.NewPatientScreen
import com.farma.parkinsoftapp.presentation.doctor.new_patient_tests.NewPatientsTestScreen
import com.farma.parkinsoftapp.presentation.doctor.patient_current_test.PatientCurrentTestScreen
import com.farma.parkinsoftapp.presentation.doctor.patient_info.PatientInfoScreen
import com.farma.parkinsoftapp.presentation.login.login_screen.LoginScreen
import com.farma.parkinsoftapp.presentation.login.sms_screen.SmsScreen
import com.farma.parkinsoftapp.presentation.patient.all_tests.PatientAllTestsScreen
import com.farma.parkinsoftapp.presentation.patient.test.PatientTestScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    userRole: UserRoleValues
) {
    NavHost(
        navController = navController,
        startDestination = when(userRole) {
            UserRoleValues.DOCTOR -> AllPatientsRoute
            UserRoleValues.PATIENT -> PatientAllTestsRoute
            UserRoleValues.UNAUTHORIZED -> LoginRoute
        }
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToSms = { phoneNumber: String, userRole: UserRole ->
                    navController.navigate(SmsRoute(phoneNumber, userRole))
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
                    when(args.userRole) {
                        UserRole.DOCTOR -> navController.navigate(AllPatientsRoute)
                        UserRole.PATIENT -> navController.navigate(PatientAllTestsRoute)
                    }
                }
            )
        }

        //Пациент
        composable<PatientAllTestsRoute> {
            PatientAllTestsScreen(
                navigateToTest = { testId: Int, testType: TestType ->
                    navController.navigate(
                        PatientTestRoute(
                            testId = testId,
                            testType = testType
                        )
                    )
                }
            )
        }
        composable<PatientTestRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientTestRoute>()

            PatientTestScreen(
                testId = args.testId,
                closeTest = {
                    navController.popBackStack()
                },
                finishTest = {
                    navController.popBackStack()
                },
            )
        }

        //Врач
        composable<AllPatientsRoute> {
            AllPatientsScreen(
                navigateToAddNewPatientScreen = {
                    navController.navigate(NewPatientRoute)
                },
                navigateToPatient = { patientId: Int ->
                    navController.navigate(PatientInfoRoute(patientId))
                }
            )
        }

        composable<NewPatientRoute> {
            NewPatientScreen(
                closeScreen = {
                    navController.popBackStack()
                },
                nextScreenNavigation = { patient ->
                    navController.navigate(
                        NewPatientTestsRoute(
                            patient.id,
                            patient.firstName,
                            patient.lastName,
                            patient.middleName,
                            patient.age,
                            patient.diagnosis,
                            patient.onTreatment,
                            patient.unreadTests,
                            patient.sex
                        )
                    )
                }
            )
        }

        composable<NewPatientTestsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<NewPatientTestsRoute>()

            NewPatientsTestScreen(
                navigationArgs = args,
                backNavigation = {
                    navController.popBackStack()
                },
                nextScreenNavigation = { patientId ->
                    navController.navigate(PatientInfoRoute(patientId = patientId)) {
                        popUpTo<NewPatientRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<PatientInfoRoute> {
            PatientInfoScreen(
                backNavigation = {
                    navController.navigate(AllPatientsRoute){
                        popUpTo(0)
                    }
                },
                navigateToTestInfo = {
                    navController.navigate(PatientCurrentTestRoute)
                }
            )
        }

        composable<PatientCurrentTestRoute> {
            PatientCurrentTestScreen(
                backNavigation = {
                    navController.popBackStack()
                },
            )
        }
    }
}

