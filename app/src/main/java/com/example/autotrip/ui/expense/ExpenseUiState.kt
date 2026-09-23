package com.example.autotrip.ui.expense

import com.example.autotrip.data.sample.ExpenseSampleData
import com.example.autotrip.data.sample.SampleExpense
import com.example.autotrip.data.sample.SampleSettlement

// 의도: 화면별 UiState. 기본값은 샘플 데이터로 채움
// TODO: 화면 채우는 단계(U3~)에서 필요한 필드 추가할 것

data class ExpenseHomeUiState(
    val tripTitle: String = ExpenseSampleData.trip.title,
    val periodText: String = ExpenseSampleData.trip.periodText,
    val memberCountText: String = ExpenseSampleData.trip.memberCountText,
    val dDayText: String = ExpenseSampleData.homeSummary.dDayText,
    val budgetText: String = ExpenseSampleData.trip.budgetPerPersonText,
    val budgetUsedRatio: Float = ExpenseSampleData.homeSummary.budgetUsedRatio,
    val prepaidTotalText: String = ExpenseSampleData.homeSummary.prepaidTotalText,
    val remainingBudgetText: String = ExpenseSampleData.homeSummary.remainingBudgetText,
    val planTotalText: String = ExpenseSampleData.homeSummary.planTotalText,
    val planStatusText: String = ExpenseSampleData.homeSummary.planStatusText,
    val cashBalanceText: String = ExpenseSampleData.homeSummary.cashBalanceText,
    val cashKrwText: String = ExpenseSampleData.homeSummary.cashKrwText,
    val settlementAmountText: String = ExpenseSampleData.homeSummary.settlementAmountText,
    val settlementSummaryText: String = ExpenseSampleData.homeSummary.settlementSummaryText,
    val recentExpenses: List<SampleExpense> = ExpenseSampleData.recentExpenses,
)

data class ExpensePlanListUiState(
    val tripTitle: String = ExpenseSampleData.trip.title,
)

data class ExpensePrepaidFormUiState(
    val memberNames: List<String> = ExpenseSampleData.trip.memberNames,
)

data class ExpenseSettlementUiState(
    val settlements: List<SampleSettlement> = ExpenseSampleData.settlements,
)

data class ExpenseRecordFormUiState(
    val editingExpense: SampleExpense? = null,
) {
    val isEditMode: Boolean get() = editingExpense != null

    companion object {
        // 의도: 09에서 넘어온 id로 샘플 지출을 찾아 수정 모드 값을 채움
        fun forExpense(expenseId: String?): ExpenseRecordFormUiState =
            ExpenseRecordFormUiState(editingExpense = ExpenseSampleData.findExpense(expenseId))
    }
}

data class ExpenseReceiptReviewUiState(
    val memberNames: List<String> = ExpenseSampleData.trip.memberNames,
)

data class ExpenseCashWalletUiState(
    val exchangeRateText: String = ExpenseSampleData.trip.exchangeRateText,
)

data class ExpenseListUiState(
    val expenses: List<SampleExpense> = ExpenseSampleData.expenses,
)

data class ExpenseFilterUiState(
    val memberNames: List<String> = ExpenseSampleData.trip.memberNames,
)

data class ExpenseTripReportUiState(
    val tripTitle: String = ExpenseSampleData.trip.title,
    val periodText: String = ExpenseSampleData.trip.periodText,
)

data class ExpenseInviteUiState(
    val tripTitle: String = ExpenseSampleData.trip.title,
    val memberNames: List<String> = ExpenseSampleData.trip.memberNames,
)
