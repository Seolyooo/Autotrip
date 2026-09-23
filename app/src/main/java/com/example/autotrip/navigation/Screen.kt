package com.example.autotrip.navigation

// TODO: 화면 경로 및 이동 로직 정의 예정

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object EmailLogin : Screen("email_login")
    data object SignUp : Screen("signup")
    data object SignUpComplete : Screen("signup_complete")
    data object GoogleLogin : Screen("google_login")
    data object Home : Screen("home")
    data object Budget : Screen("budget")
    data object BudgetDetail : Screen("budget_detail")
}
