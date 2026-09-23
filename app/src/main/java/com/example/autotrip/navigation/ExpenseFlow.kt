package com.example.autotrip.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.autotrip.ui.expense.ExpenseCashWalletScreen
import com.example.autotrip.ui.expense.ExpenseCashWalletUiState
import com.example.autotrip.ui.expense.ExpenseFilterSheet
import com.example.autotrip.ui.expense.ExpenseFilterUiState
import com.example.autotrip.ui.expense.ExpenseHomeScreen
import com.example.autotrip.ui.expense.ExpenseHomeUiState
import com.example.autotrip.ui.expense.ExpenseInviteScreen
import com.example.autotrip.ui.expense.ExpenseInviteUiState
import com.example.autotrip.ui.expense.ExpenseListScreen
import com.example.autotrip.ui.expense.ExpenseListUiState
import com.example.autotrip.ui.expense.ExpensePlanListScreen
import com.example.autotrip.ui.expense.ExpensePlanListUiState
import com.example.autotrip.ui.expense.ExpensePrepaidFormScreen
import com.example.autotrip.ui.expense.ExpensePrepaidFormUiState
import com.example.autotrip.ui.expense.ExpenseReceiptReviewScreen
import com.example.autotrip.ui.expense.ExpenseReceiptReviewUiState
import com.example.autotrip.ui.expense.ExpenseRecordFormScreen
import com.example.autotrip.ui.expense.ExpenseRecordFormUiState
import com.example.autotrip.ui.expense.ExpenseSettlementScreen
import com.example.autotrip.ui.expense.ExpenseSettlementUiState
import com.example.autotrip.ui.expense.ExpenseTripReportScreen
import com.example.autotrip.ui.expense.ExpenseTripReportUiState

// 의도: 경비 화면 전환을 이 파일 하나에서만 처리함. 화면 함수는 콜백만 받고 서로 모름
// 임시: 백스택이 remember라 화면 회전 시 홈으로 돌아감. NavController로 바꿀 때 해결할 것
@Composable
fun ExpenseFlow(onExit: () -> Unit) {
    val backStack = remember { mutableStateListOf<ExpenseRoute>(ExpenseRoute.Home) }

    fun navigate(route: ExpenseRoute) {
        backStack.add(route)
    }

    // 의도: 홈만 남은 상태에서 뒤로 가면 경비 흐름 자체를 빠져나감
    fun pop() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else onExit()
    }

    BackHandler { pop() }

    when (val current = backStack.last()) {
        ExpenseRoute.Home -> ExpenseHomeScreen(
            state = ExpenseHomeUiState(),
            onBack = { pop() },
            onRecordClick = { navigate(ExpenseRoute.RecordForm()) },
            onCameraClick = { navigate(ExpenseRoute.ReceiptReview) },
            onCashWalletClick = { navigate(ExpenseRoute.CashWallet) },
            onSettlementClick = { navigate(ExpenseRoute.Settlement) },
            onPlanListClick = { navigate(ExpenseRoute.PlanList) },
            onPrepaidClick = { navigate(ExpenseRoute.PrepaidForm) },
            onExpenseListClick = { navigate(ExpenseRoute.ExpenseList) },
            onTripReportClick = { navigate(ExpenseRoute.TripReport) },
        )

        ExpenseRoute.PlanList -> ExpensePlanListScreen(
            state = ExpensePlanListUiState(),
            onBack = { pop() },
            onConvertToPrepaidClick = { navigate(ExpenseRoute.PrepaidForm) },
        )

        ExpenseRoute.PrepaidForm -> ExpensePrepaidFormScreen(
            state = ExpensePrepaidFormUiState(),
            onBack = { pop() },
            onSaveClick = { pop() },
        )

        ExpenseRoute.Settlement -> ExpenseSettlementScreen(
            state = ExpenseSettlementUiState(),
            onBack = { pop() },
            onInviteClick = { navigate(ExpenseRoute.Invite) },
        )

        is ExpenseRoute.RecordForm -> ExpenseRecordFormScreen(
            state = ExpenseRecordFormUiState.forExpense(current.editingExpenseId),
            onBack = { pop() },
            onCameraClick = { navigate(ExpenseRoute.ReceiptReview) },
            onSaveClick = { pop() },
        )

        ExpenseRoute.ReceiptReview -> ExpenseReceiptReviewScreen(
            state = ExpenseReceiptReviewUiState(),
            onBack = { pop() },
            onConfirmClick = {
                // 의도: 06을 거쳐 왔으면 06까지 닫아서 저장 완료 상태로 돌아감
                backStack.removeAt(backStack.lastIndex)
                if (backStack.size > 1 && backStack.last() is ExpenseRoute.RecordForm) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
        )

        ExpenseRoute.CashWallet -> ExpenseCashWalletScreen(
            state = ExpenseCashWalletUiState(),
            onBack = { pop() },
        )

        // 의도: 10은 09 위에 뜨는 시트라 09를 그대로 그리고 그 위에 시트를 올림
        ExpenseRoute.ExpenseList, ExpenseRoute.FilterSheet -> {
            ExpenseListScreen(
                state = ExpenseListUiState(),
                onBack = { pop() },
                onFilterClick = { navigate(ExpenseRoute.FilterSheet) },
                onExpenseClick = { expenseId -> navigate(ExpenseRoute.RecordForm(expenseId)) },
            )
            if (current == ExpenseRoute.FilterSheet) {
                ExpenseFilterSheet(
                    state = ExpenseFilterUiState(),
                    onBack = { pop() },
                )
            }
        }

        ExpenseRoute.TripReport -> ExpenseTripReportScreen(
            state = ExpenseTripReportUiState(),
            onBack = { pop() },
            onSettlementClick = { navigate(ExpenseRoute.Settlement) },
            onShareClick = { navigate(ExpenseRoute.Invite) },
        )

        ExpenseRoute.Invite -> ExpenseInviteScreen(
            state = ExpenseInviteUiState(),
            onBack = { pop() },
        )
    }
}
