package com.example.autotrip.navigation

// 의도: 경비 화면 이름만 정의함. 나중에 NavController로 바꿔도 화면 함수는 그대로 쓰게 함
sealed class ExpenseRoute {
    data object Home : ExpenseRoute()                  // 01
    data object PlanList : ExpenseRoute()              // 02
    data object PrepaidForm : ExpenseRoute()           // 03
    data object Settlement : ExpenseRoute()            // 04

    // 의도: editingExpenseId가 있으면 09에서 누른 항목을 고치는 수정 모드
    data class RecordForm(val editingExpenseId: String? = null) : ExpenseRoute() // 06

    data object ReceiptReview : ExpenseRoute()         // 07
    data object CashWallet : ExpenseRoute()            // 08
    data object ExpenseList : ExpenseRoute()           // 09
    data object FilterSheet : ExpenseRoute()           // 10, 09 위 바텀시트
    data object TripReport : ExpenseRoute()            // 11
    data object Invite : ExpenseRoute()                // 12
}
