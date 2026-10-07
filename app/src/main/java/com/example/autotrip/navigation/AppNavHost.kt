package com.example.autotrip.navigation

// TODO: 화면 경로 및 이동 로직 정의 예정

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autotrip.ui.auth.LoginScreen
import com.example.autotrip.ui.auth.SignUpScreen
import com.example.autotrip.ui.auth.SplashScreen
import com.example.autotrip.ui.budget.BudgetDetailScreen
import com.example.autotrip.ui.budget.BudgetMainScreen
import com.example.autotrip.ui.home.HomeScreen

/**
 * 참고용으로 만들어둔 NavHost 구현체.
 * 임시: 지금 MainActivity.kt는 when문 기반으로 화면 전환 중이라 여기서 바로 안 덮어씀.
 * Navigation Compose로 전환하기로 정해지면 MainActivity의 setContent 안을 아래처럼 바꿀 것:
 *
 *   setContent { AutoTripTheme { AppNavHost() } }
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Splash.route) {
            SplashScreen()
            // TODO: 2.5초 후 자동으로 Login으로 이동하는 로직 붙일 것.
            // 기존 MainActivity의 LaunchedEffect + delay(2500) 참고해서 이식 가능
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onSignUpComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToBudget = { navController.navigate(Screen.Budget.route) }
            )
        }

        composable(Screen.Budget.route) {
            BudgetMainScreen(
                onBackClick = { navController.popBackStack() },
                onSeeDetailClick = { navController.navigate(Screen.BudgetDetail.route) }
                // 임시: onScanReceiptClick, onManualAddClick은 화면 아직 없어서 안 붙임. 다음 단계에서 연결할 것
            )
        }

        composable(Screen.BudgetDetail.route) {
            BudgetDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
