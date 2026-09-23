package com.example.autotrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.autotrip.ui.auth.EmailLoginScreen
import com.example.autotrip.ui.auth.GoogleLoginScreen
import com.example.autotrip.ui.auth.LoginScreen
import com.example.autotrip.ui.auth.SignUpCompleteScreen
import com.example.autotrip.ui.auth.SignUpScreen
import com.example.autotrip.ui.auth.SplashScreen
import com.example.autotrip.ui.budget.BudgetDetailScreen
import com.example.autotrip.ui.budget.BudgetMainScreen
import com.example.autotrip.ui.budget.ExpenseEntryScreen
import com.example.autotrip.ui.home.HomeScreen
import com.example.autotrip.ui.plan.PlanScreen
import com.example.autotrip.ui.plan.TripCreateScreen
import com.example.autotrip.ui.theme.AutoTripTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutoTripTheme {

                // 화면 상태
                var showSplash by remember { mutableStateOf(true) }
                var showHome by remember { mutableStateOf(false) }
                var showPlan by remember { mutableStateOf(false) }
                var showTripCreate by remember { mutableStateOf(false) }
                var showBudget by remember { mutableStateOf(false) }
                var showBudgetDetail by remember { mutableStateOf(false) }
                var showExpense by remember { mutableStateOf(false) }
                var showSignUp by remember { mutableStateOf(false) }
                var showEmailLogin by remember { mutableStateOf(false) }
                var showSignUpComplete by remember { mutableStateOf(false) }
                var showGoogleLogin by remember { mutableStateOf(false) }

                // Splash 2.5초
                LaunchedEffect(Unit) {
                    delay(2500)
                    showSplash = false
                }

                when {

                    // 1. Splash
                    showSplash -> {
                        SplashScreen()
                    }

                    // 2. 여행 정보 입력
                    showTripCreate -> {
                        TripCreateScreen()
                    }

                    // 3. 여행 계획 안내
                    showPlan -> {
                        PlanScreen(
                            onBackClick = {
                                showPlan = false
                            },
                            onStartClick = {
                                showPlan = false
                                showTripCreate = true
                            }
                        )
                    }

                    // 4. 가계부 상세내역
                    showBudgetDetail -> {
                        BudgetDetailScreen(
                            onBackClick = {
                                showBudgetDetail = false
                            }
                        )
                    }

                    // 5. 지출 직접 입력
                    showExpense -> {
                        ExpenseEntryScreen(
                            onBackClick = {
                                showExpense = false
                            },
                            onSaveClick = {
                                showExpense = false
                            }
                        )
                    }

                    // 6. 가계부
                    showBudget -> {
                        BudgetMainScreen(
                            onBackClick = {
                                showBudget = false
                            },
                            onSeeDetailClick = {
                                showBudgetDetail = true
                            },
                            onManualAddClick = {
                                showExpense = true
                            }
                        )
                    }

                    // 5. 메인
                    showHome -> {
                        HomeScreen(
                            onTravelClick = {
                                showHome = true
                                showPlan = true
                            },
                            onAddClick = {
                                showPlan = true
                            },
                            onNavigateToBudget = {
                                showBudget = true
                            }
                        )
                    }

                    // 7. 회원가입 완료
                    showSignUpComplete -> {
                        SignUpCompleteScreen(
                            onConfirmClick = {
                                showSignUpComplete = false
                                showEmailLogin = true
                            }
                        )
                    }

                    // 6. 회원가입
                    showSignUp -> {
                        SignUpScreen(
                            onBackClick = {
                                showSignUp = false
                            },
                            onSignUpComplete = {
                                showSignUp = false
                                showSignUpComplete = true
                            }
                        )
                    }

                    // 5-2. 이메일 로그인
                    showEmailLogin -> {
                        EmailLoginScreen(
                            onBackClick = {
                                showEmailLogin = false
                            },
                            onLoginClick = {
                                showEmailLogin = false
                                showHome = true
                            },
                            onFindIdClick = {
                                // TODO: 아이디 찾기 화면 연결
                            },
                            onFindPasswordClick = {
                                // TODO: 비밀번호 찾기 화면 연결
                            }
                        )
                    }

                    // Google 로그인 중간 화면
                    showGoogleLogin -> {
                        GoogleLoginScreen(
                            onGoogleContinueClick = {
                                showGoogleLogin = false
                                showHome = true
                            }
                        )
                    }

                    // 5. 로그인
                    else -> {
                        LoginScreen(
                            onLoginClick = {
                                showGoogleLogin = true
                            },
                            onEmailLoginClick = {
                                showEmailLogin = true
                            },
                            onSignUpClick = {
                                showSignUp = true
                            }
                        )
                    }
                }
            }
        }
    }
}