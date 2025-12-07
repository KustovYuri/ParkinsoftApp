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
import com.farma.parkinsoftapp.data.network.models.LoginModel
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.domain.models.user.UserRole
import com.farma.parkinsoftapp.presentation.doctor.all_patients.AllPatientsScreen
import com.farma.parkinsoftapp.presentation.doctor.new_pacient.NewPatientScreen
import com.farma.parkinsoftapp.presentation.doctor.new_patient_tests.NewPatientsTestScreen
import com.farma.parkinsoftapp.presentation.doctor.patient_current_test.native_test.PatientCurrentNativeTestScreen
import com.farma.parkinsoftapp.presentation.doctor.patient_current_test.PatientCurrentTestScreen
import com.farma.parkinsoftapp.presentation.doctor.patient_info.PatientInfoScreen
import com.farma.parkinsoftapp.presentation.login.login_screen.LoginScreen
import com.farma.parkinsoftapp.presentation.login.sms_screen.SmsScreen
import com.farma.parkinsoftapp.presentation.patient.all_tests.PatientAllTestsScreen
import com.farma.parkinsoftapp.presentation.patient.test.dn_4.Dn4Screen
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.PainDetectedScreen
import com.farma.parkinsoftapp.presentation.patient.test.sf_36.Sf36Screen
import com.farma.parkinsoftapp.presentation.patient.test.single_answer.PatientTestScreen
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.TestStimulationScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    userRole: UserRoleValues,
) {
    NavHost(
        navController = navController,
        startDestination =
            //        PatientTestRoute(1, TestType.TEST_STIMULATION_DIARY)
            when (userRole) {
                UserRoleValues.DOCTOR -> AllPatientsRoute
                UserRoleValues.PATIENT -> PatientAllTestsRoute
                UserRoleValues.UNAUTHORIZED -> LoginRoute
            }
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToSms = { phoneNumber: String, loginModel: LoginModel ->
                    navController.navigate(
                        SmsRoute(
                            phoneNumber,
                            loginModel.userId,
                            UserRole.valueOf(loginModel.role)
                        )
                    )
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
                navigationToDoctor = {
                    navController.navigate(AllPatientsRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigationToPatient = {
                    navController.navigate(PatientAllTestsRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        //Пациент
        composable<PatientAllTestsRoute> {
            PatientAllTestsScreen(
                navigateToTest = { testId: Long, testType: TestType ->
                    navController.navigate(
                        PatientTestRoute(
                            testId = testId,
                            testType = testType
                        )
                    )
                },
                navigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<PatientTestRoute> { backStackEntry ->
            val testType = backStackEntry.toRoute<PatientTestRoute>().testType

            when (testType) {
                TestType.HADS1, TestType.HADS2, TestType.OSVESTRY, TestType.LANSS -> {
                    PatientTestScreen(
                        closeTest = {
                            navController.popBackStack()
                        },
                        finishTest = {
                            navController.navigate(PatientAllTestsRoute) {
                                popUpTo<PatientAllTestsRoute> {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }

                TestType.DN4 -> {
                    Dn4Screen(
                        closeTest = {
                            navController.popBackStack()
                        },
                        finishTest = {
                            navController.navigate(PatientAllTestsRoute) {
                                popUpTo<PatientAllTestsRoute> {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }

                TestType.SF36 -> {
                    Sf36Screen(
                        closeTest = {
                            navController.popBackStack()
                        },
                        finishTest = {
                            navController.navigate(PatientAllTestsRoute) {
                                popUpTo<PatientAllTestsRoute> {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }

                TestType.TEST_STIMULATION_DIARY -> {
                    TestStimulationScreen(
                        closeTest = {
                            navController.popBackStack()
                        },
                        finishTest = {
                            navController.navigate(PatientAllTestsRoute) {
                                popUpTo<PatientAllTestsRoute> {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }

                TestType.PAIN_DETECTED -> {
                    PainDetectedScreen(
                        closeTest = {
                            navController.popBackStack()
                        },
                        finishTest = {
                            navController.navigate(PatientAllTestsRoute) {
                                popUpTo<PatientAllTestsRoute> {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
            }
        }

        //Врач
        composable<AllPatientsRoute> {
            AllPatientsScreen(
                navigateToAddNewPatientScreen = { doctorId: Long ->
                    navController.navigate(NewPatientRoute(doctorId))
                },
                navigateToPatient = { patientId: Long ->
                    navController.navigate(PatientInfoRoute(patientId))
                },
                navigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<NewPatientRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<NewPatientRoute>()

            NewPatientScreen(
                closeScreen = {
                    navController.popBackStack()
                },
                nextScreenNavigation = { patient ->
                    navController.navigate(
                        NewPatientTestsRoute(
                            id = patient.id,
                            doctorId = args.doctorId,
                            firstName = patient.firstName,
                            lastName = patient.lastName,
                            middleName = patient.middleName,
                            age = patient.age,
                            phoneNumber = patient.phoneNumber,
                            birthdayDate = patient.birthdayDate,
                            diagnosis = patient.diagnosis,
                            onTreatment = patient.onTreatment,
                            unreadTests = patient.unreadTests,
                            sex = patient.sex
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
                    navController.navigate(AllPatientsRoute) {
                        popUpTo(0)
                    }
                },
                navigateToTestInfo = {
                    initials: String,
                    testDate: String,
                    testType: TestType,
                    testPreviewId: Long,
                    isNativeTest: Boolean,
                    maxPoints: Int,
                    summaryPoints: Int,
                    pf: Float?,
                    rp: Float?,
                    bp: Float?,
                    gh: Float?,
                    vt: Float?,
                    sf: Float?,
                    re: Float?,
                    mh: Float? ->

                    navController.navigate(
                        PatientCurrentTestRoute(
                            initials,
                            testDate,
                            testType,
                            testPreviewId,
                            isNativeTest,
                            maxPoints,
                            summaryPoints,
                            pf,
                            rp,
                            bp,
                            gh,
                            vt,
                            sf,
                            re,
                            mh,
                        )
                    )
                }
            )
        }

        composable<PatientCurrentTestRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<PatientCurrentTestRoute>()

            if (args.isNativeTest) {
                PatientCurrentNativeTestScreen(
                    patientInitials = args.initials,
                    backNavigation = {
                        navController.popBackStack()
                    },
                    testDate = args.testDate,
                    maxPoints = args.maxScore,
                    summaryPoints = args.currentScore,
                    pf = args.pf,
                    rp = args.rp,
                    bp = args.bp,
                    gh = args.gh,
                    vt = args.vt,
                    sf = args.sf,
                    re = args.re,
                    mh = args.mh,
                )
            } else {
                PatientCurrentTestScreen(
                    patientInitials = args.initials,
                    backNavigation = {
                        navController.popBackStack()
                    },
                    testDate = args.testDate,
                    maxPoints = args.maxScore,
                    summaryPoints = args.currentScore,
                )
            }
        }
    }
}

