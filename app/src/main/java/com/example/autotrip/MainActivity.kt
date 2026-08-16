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
import com.example.autotrip.ui.auth.LoginScreen
import com.example.autotrip.ui.auth.SplashScreen
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

                    // 4. 메인
                    showHome -> {
                        HomeScreen(
                            onTravelClick = {
                                showHome = true
                                showPlan = true
                            }
                        )
                    }

                    // 5. 로그인
                    else -> {
                        LoginScreen(
                            onLoginClick = {
                                showHome = true
                            }
                        )
                    }
                }
            }
        }
    }
}