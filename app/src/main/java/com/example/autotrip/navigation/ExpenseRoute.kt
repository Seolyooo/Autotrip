package com.example.autotrip.navigation

// 비용(Expense) 화면 경로 및 이동 로직 정의
// 의도: 경비 화면 이름만 정의함. 나중에 NavController로 바꿔도 화면 함수는 그대로 쓰게 함
sealed class ExpenseRoute {
    data object Home : ExpenseRoute()                  // 01
    data object PlanList : ExpenseRoute()              // 02
    data object Settlement : ExpenseRoute()            // 04

    // 의도: 03 사전 결제와 06 지출 기록을 합친 화면
    // editingExpenseId가 있으면 09에서 누른 항목을 고치는 수정 모드, planItemId가 있으면 02에서 넘어온 계획 항목 연결
    data class RecordForm(val editingExpenseId: String? = null, val planItemId: String? = null) : ExpenseRoute() // 03+06

    data object ReceiptReview : ExpenseRoute()         // 07
    data object CashWallet : ExpenseRoute()            // 08
    data object ExpenseList : ExpenseRoute()           // 09
    data object FilterSheet : ExpenseRoute()           // 10, 09 위 바텀시트
    data object TripReport : ExpenseRoute()            // 11
    data object Invite : ExpenseRoute()                // 12
}
