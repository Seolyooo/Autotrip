package com.example.autotrip.ui.expense

// 02 계획 금액

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.autotrip.data.sample.SamplePlanItem

// 빠짐: 상단 '+' (계획 항목 추가) 버튼 — 이동할 화면이 없어 보류
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensePlanListScreen(
    state: ExpensePlanListUiState,
    onBack: () -> Unit,
    onConvertToPrepaidClick: (planItemId: String) -> Unit,
) {
    // 의도: 탭 전환은 화면 안 상태. '전체'가 아니면 statusFilterText가 같은 항목만 보여줌
    var selectedTab by remember(state) { mutableStateOf(state.statusFilterOptions.first()) }
    val visibleItems = if (selectedTab == state.statusFilterOptions.first()) {
        state.planItems
    } else {
        state.planItems.filter { it.statusFilterText == selectedTab }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("계획 금액") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "여행 계획하면서 조사한 금액을 모아둬요. 결제하면 '결제로 전환'.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            "계획 합계 (내 몫)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(state.planTotalText, style = MaterialTheme.typography.headlineMedium)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "예산",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(state.budgetText, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                state.statusFilterOptions.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = selectedTab == option,
                        onClick = { selectedTab = option },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = state.statusFilterOptions.size),
                    ) { Text(option) }
                }
            }

            visibleItems.forEach { item ->
                PlanItemCard(item = item, onConvertClick = { onConvertToPrepaidClick(item.id) })
            }
        }
    }
}

@Composable
private fun PlanItemCard(
    item: SamplePlanItem,
    onConvertClick: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("${item.categoryText} · ${item.title}", style = MaterialTheme.typography.titleMedium)
            Text(item.amountLineText, style = MaterialTheme.typography.bodyMedium)
            item.noteLineText?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when {
                    item.paidBadgeText != null -> {
                        val text = if (item.savedAmountText != null) {
                            "${item.paidBadgeText} · ${item.savedAmountText}"
                        } else {
                            item.paidBadgeText
                        }
                        Text(text, style = MaterialTheme.typography.bodyLarge)
                        if (item.paidBadgeChecked) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.padding(start = 4.dp),
                            )
                        }
                    }

                    item.trackingBadgeText != null -> Text(
                        item.trackingBadgeText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    item.isConvertible -> OutlinedButton(onClick = onConvertClick) {
                        Text("결제로 전환")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpensePlanListScreenPreview() {
    ExpensePlanListScreen(
        state = ExpensePlanListUiState(),
        onBack = {},
        onConvertToPrepaidClick = {},
    )
}
