package com.example.autotrip.ui.expense

// 09 소비 목록

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleExpense
import com.example.autotrip.data.sample.SampleExpenseDayGroup

// 의도: '내 부담액'/'결제 금액' 탭 두 가지. 09에선 이 상태만으로 표시 값을 바꿔 보여줌
private const val TAB_MY_BURDEN = "내 부담액"
private const val TAB_PAID_AMOUNT = "결제 금액"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListUiState,
    onBack: () -> Unit,
    onFilterClick: () -> Unit,
    onExpenseClick: (expenseId: String) -> Unit,
) {
    // 의도: 탭 전환은 화면 안 상태. 값 자체는 샘플에서 그대로 가져다 씀 (계산 없음)
    var selectedTab by remember(state) { mutableStateOf(TAB_MY_BURDEN) }
    val isMyBurdenMode = selectedTab == TAB_MY_BURDEN

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("소비 목록") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    // 빠짐: 검색 아이콘 — 이동할 화면이 없어 보류
                    IconButton(onClick = onFilterClick) {
                        Icon(Icons.Filled.FilterList, contentDescription = "정렬·필터")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // TODO: 여행 전환 드롭다운은 여행 목록 화면이 생기면 추가할 것 (01 홈과 동일)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(state.tripTitle, style = MaterialTheme.typography.titleLarge)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                listOf(TAB_MY_BURDEN, TAB_PAID_AMOUNT).forEachIndexed { index, tab ->
                    SegmentedButton(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                    ) { Text(tab) }
                }
            }

            // 의도: 아래 칩은 전부 현재 필터 상태만 보여줌. 어떤 칩을 눌러도 10번 시트가 열림
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = onFilterClick,
                    label = { Text(state.sortLabelText) },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                )
                FilterChip(
                    selected = state.isSplitOnlyActive,
                    onClick = onFilterClick,
                    label = { Text("N빵만") },
                )
                state.payMethodQuickOptions.forEach { option ->
                    FilterChip(selected = false, onClick = onFilterClick, label = { Text(option) })
                }
                AssistChip(
                    onClick = onFilterClick,
                    label = { Text(state.payerLabelText) },
                    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                )
            }

            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "${selectedTab} 합계 · ${state.totalCountText}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    // 임시: '결제 금액' 탭도 합계는 같은 샘플 값을 그대로 씀 (계산 없음)
                    Text(state.totalAmountText, style = MaterialTheme.typography.titleLarge)
                }
            }

            state.dayGroups.forEach { group ->
                ExpenseDayGroupSection(
                    group = group,
                    isMyBurdenMode = isMyBurdenMode,
                    onExpenseClick = onExpenseClick,
                )
            }
        }
    }
}

@Composable
private fun ExpenseDayGroupSection(
    group: SampleExpenseDayGroup,
    isMyBurdenMode: Boolean,
    onExpenseClick: (expenseId: String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(group.dateLabel, style = MaterialTheme.typography.titleSmall)
            // 임시: '결제 금액' 탭용 날짜별 합계 샘플이 없어 '내 부담액' 탭에서만 보여줌
            if (isMyBurdenMode) {
                Text(group.myBurdenSubtotalText, style = MaterialTheme.typography.bodyMedium)
            }
        }
        group.expenses.forEach { expense ->
            ExpenseListItemCard(
                expense = expense,
                isMyBurdenMode = isMyBurdenMode,
                onClick = { onExpenseClick(expense.id) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpenseListItemCard(
    expense: SampleExpense,
    isMyBurdenMode: Boolean,
    onClick: () -> Unit,
) {
    val primaryAmountText = if (isMyBurdenMode) expense.myBurdenAmountText else expense.amountText
    val secondaryKrwText = if (isMyBurdenMode) {
        expense.myBurdenKrwText
    } else {
        expense.krwAmountText.takeIf { it != expense.amountText }
    }
    val totalLineText = expense.totalAmountText.takeIf { isMyBurdenMode }
    val payMethodChipText = if (expense.isPrepaid) {
        "${expense.payMethodText} ${expense.paidDateText}"
    } else {
        expense.payMethodText
    }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            listOf(expense.categoryText, expense.title).filter { it.isNotEmpty() }.joinToString(" · "),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        if (expense.timeText.isNotEmpty()) {
                            Text(
                                expense.timeText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(primaryAmountText, style = MaterialTheme.typography.titleMedium)
                    secondaryKrwText?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    totalLineText?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = onClick, label = { Text(payMethodChipText) })
                expense.splitChipText?.let { AssistChip(onClick = onClick, label = { Text(it) }) }
                expense.payerChipText?.let { AssistChip(onClick = onClick, label = { Text(it) }) }
                expense.settlementChipText?.let { AssistChip(onClick = onClick, label = { Text(it) }) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseListScreenPreview() {
    ExpenseListScreen(
        state = ExpenseListUiState(),
        onBack = {},
        onFilterClick = {},
        onExpenseClick = {},
    )
}
