package com.example.autotrip.ui.expense

// 01 경비 홈

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleExpense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseHomeScreen(
    state: ExpenseHomeUiState,
    onBack: () -> Unit,
    onRecordClick: () -> Unit,
    onCameraClick: () -> Unit,
    onCashWalletClick: () -> Unit,
    onSettlementClick: () -> Unit,
    onPlanListClick: () -> Unit,
    onPrepaidClick: () -> Unit,
    onExpenseListClick: () -> Unit,
    onTripReportClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("여행 경비") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    // 의도: 와이어프레임에 없는 리포트 진입점이라 상단 아이콘으로 추가함
                    IconButton(onClick = onTripReportClick) {
                        Icon(Icons.Filled.Assessment, contentDescription = "여행 리포트")
                    }
                },
            )
        },
        floatingActionButton = {
            // 의도: 시스템 내비게이션 바에 FAB이 가려지지 않게 navigationBarsPadding 적용
            Row(
                modifier = Modifier.navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FloatingActionButton(onClick = onCameraClick) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "영수증 촬영")
                }
                ExtendedFloatingActionButton(
                    onClick = onRecordClick,
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text("기록") },
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TripHeader(state)
            BudgetCard(state)
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CashWalletCard(
                    state = state,
                    onClick = onCashWalletClick,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                SettlementCard(
                    state = state,
                    onClick = onSettlementClick,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickActionButton(Icons.Filled.Edit, "계획 금액", onPlanListClick, Modifier.weight(1f))
                QuickActionButton(Icons.Filled.Check, "사전 결제", onPrepaidClick, Modifier.weight(1f))
                QuickActionButton(Icons.Filled.AccountBalanceWallet, "환전 기록", onCashWalletClick, Modifier.weight(1f))
                QuickActionButton(Icons.Filled.FilterList, "소비 목록", onExpenseListClick, Modifier.weight(1f))
            }
            RecentExpenseSection(
                expenses = state.recentExpenses,
                onSeeAllClick = onExpenseListClick,
            )
            // 의도: 마지막 항목이 FAB에 가려지지 않게 아래 여백 둠
            Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
private fun TripHeader(state: ExpenseHomeUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // TODO: 여행 전환 드롭다운은 여행 목록 화면이 생기면 추가할 것
        Text(state.tripTitle, style = MaterialTheme.typography.headlineSmall)
        Text(
            listOf(state.periodText, state.memberCountText, state.dDayText).joinToString(" · "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BudgetCard(state: ExpenseHomeUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("내 예산", style = MaterialTheme.typography.labelLarge)
            Text(state.budgetText, style = MaterialTheme.typography.headlineMedium)
            LinearProgressIndicator(
                progress = { state.budgetUsedRatio },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                "사전 결제 ${state.prepaidTotalText} · 남은 예산 ${state.remainingBudgetText}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                "계획 금액 ${state.planTotalText} → ${state.planStatusText}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun CashWalletCard(
    state: ExpenseHomeUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null)
                Text("현금 지갑", style = MaterialTheme.typography.labelLarge)
            }
            Text(state.cashBalanceText, style = MaterialTheme.typography.titleLarge)
            Text("≈ ${state.cashKrwText}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettlementCard(
    state: ExpenseHomeUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("정산", style = MaterialTheme.typography.labelLarge)
            Text(state.settlementAmountText, style = MaterialTheme.typography.titleLarge)
            Text(state.settlementSummaryText, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(icon, contentDescription = null)
            Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1)
        }
    }
}

@Composable
private fun RecentExpenseSection(
    expenses: List<SampleExpense>,
    onSeeAllClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("최근 기록", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            TextButton(onClick = onSeeAllClick) { Text("전체 보기") }
        }
        HorizontalDivider()
        expenses.forEach { expense ->
            RecentExpenseRow(expense)
            HorizontalDivider()
        }
    }
}

@Composable
private fun RecentExpenseRow(expense: SampleExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 임시: 카테고리 썸네일 자리. 이미지 정해지면 교체할 것
        Surface(
            modifier = Modifier.size(40.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) { Box(Modifier.fillMaxSize()) }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                listOf(expense.categoryText, expense.title).filter { it.isNotEmpty() }.joinToString(" · "),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "${expense.payerName} 결제 · ${expense.splitText} · 결제 ${expense.paidDateText}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(expense.krwAmountText, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseHomeScreenPreview() {
    ExpenseHomeScreen(
        state = ExpenseHomeUiState(),
        onBack = {},
        onRecordClick = {},
        onCameraClick = {},
        onCashWalletClick = {},
        onSettlementClick = {},
        onPlanListClick = {},
        onPrepaidClick = {},
        onExpenseListClick = {},
        onTripReportClick = {},
    )
}
