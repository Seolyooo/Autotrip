package com.example.autotrip.ui.expense

import com.example.autotrip.data.sample.ExpenseSampleData
import com.example.autotrip.data.sample.SampleExpense
import com.example.autotrip.data.sample.SampleRecordForm
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

data class ExpenseSettlementUiState(
    val settlements: List<SampleSettlement> = ExpenseSampleData.settlements,
)

// 의도: 03 사전 결제 + 06 지출 기록을 합친 화면. 여행 전이면 사전 결제, 첫날부터는 지출 기록으로 보임
// 논의 필요: 저장 시 사전 결제 분류는 오늘이 아니라 결제일 < 여행 시작일로 정할 것 (기능 단계)
data class ExpenseRecordFormUiState(
    val editingExpense: SampleExpense? = null,
    val isBeforeTrip: Boolean = ExpenseSampleData.trip.isBeforeTrip,
    val form: SampleRecordForm = defaultForm(isBeforeTrip),
    val planLinkOptions: List<String> = ExpenseSampleData.planLinkOptions,
    val currencyOptions: List<String> = ExpenseSampleData.currencyOptions,
    val payMethodOptions: List<String> = ExpenseSampleData.payMethodOptions,
    val categoryOptions: List<String> = ExpenseSampleData.categoryOptions,
    val splitModeOptions: List<String> = ExpenseSampleData.splitModeOptions,
    val memberNames: List<String> = ExpenseSampleData.trip.memberNames,
) {
    val isEditMode: Boolean get() = editingExpense != null

    // 의도: 수정 모드는 항목 종류로, 새 기록은 여행 전후로 제목을 정함
    val titleText: String
        get() = when {
            editingExpense != null -> if (editingExpense.isPrepaid) "사전 결제 수정" else "지출 수정"
            isBeforeTrip -> "사전 결제 기록"
            else -> "지출 기록"
        }

    companion object {
        private fun defaultForm(isBeforeTrip: Boolean): SampleRecordForm =
            if (isBeforeTrip) ExpenseSampleData.prepaidDraft else ExpenseSampleData.duringTripDraft

        // 의도: 09에서 넘어온 id로 샘플 지출을 찾아 수정 모드 값을 채움
        fun forExpense(expenseId: String?): ExpenseRecordFormUiState {
            val expense = ExpenseSampleData.findExpense(expenseId)
                ?: return ExpenseRecordFormUiState()
            val form = ExpenseSampleData.findRecordForm(expense.id)
                ?: return ExpenseRecordFormUiState()
            return ExpenseRecordFormUiState(editingExpense = expense, form = form)
        }
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
