package com.example.autotrip.ui.expense

// 11 여행 리포트

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleTripReportCategoryAmount
import com.example.autotrip.data.sample.SampleTripReportTopSpending

// 빠짐: 여행 이름 옆 편집(연필) 아이콘 — 이동할 화면이 없어 보류 (다른 화면들과 동일한 관례)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTripReportScreen(
    state: ExpenseTripReportUiState,
    onBack: () -> Unit,
    onSettlementClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    // 의도: '내 기준 / 3명 전체' 탭 전환은 화면 안 상태
    var selectedTabIndex by remember(state) { mutableStateOf(0) }
    val basis = state.basisTabs[selectedTabIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("여행 리포트") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Filled.Share, contentDescription = "공유")
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
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(state.tripTitle, style = MaterialTheme.typography.titleLarge)
                Text(
                    "${state.periodText} · ${state.memberCountText}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                state.basisTabs.forEachIndexed { index, tab ->
                    SegmentedButton(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = state.basisTabs.size),
                    ) { Text(tab.labelText) }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        basis.spentLabelText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(basis.spentAmountText, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        basis.averageText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            basis.budgetCompareText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(basis.remainingBudgetText, style = MaterialTheme.typography.titleMedium)
                    }
                    LinearProgressIndicator(
                        progress = { basis.usedRatio },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        basis.usedPercentText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("가장 큰 지출", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider()
                    state.topSpending.forEach { item -> TopSpendingRow(item) }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("어디에 썼나", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider()
                    CategoryBreakdownBar(state.categoryBreakdown)
                    CategoryBreakdownLegend(state.categoryBreakdown)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(onClick = onSettlementClick, modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "정산",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(state.settlementRemainingText, style = MaterialTheme.typography.titleMedium)
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "남은 현금",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(state.remainingCashText, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopSpendingRow(item: SampleTripReportTopSpending) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("${item.scopeLabel} · ${item.titleText}", style = MaterialTheme.typography.bodyLarge)
            Text(
                item.detailText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(item.amountText, style = MaterialTheme.typography.titleMedium)
    }
}

// 의도: 하나의 막대를 100%로 두고 카테고리별 barRatio만큼 이어 붙임. 색은 새로 만들지 않고 onSurface 진하기만 다르게 씀
@Composable
private fun CategoryBreakdownBar(categories: List<SampleTripReportCategoryAmount>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(4.dp)),
    ) {
        categories.forEachIndexed { index, category ->
            Box(
                modifier = Modifier
                    .weight(category.barRatio)
                    .fillMaxHeight()
                    .background(categorySwatchColor(index)),
            )
        }
    }
}

// 의도: 막대 아래 카테고리명·금액을 2열로 보여줌. 각 색 점은 위 막대와 같은 순서·진하기를 씀
@Composable
private fun CategoryBreakdownLegend(categories: List<SampleTripReportCategoryAmount>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.withIndex().toList().chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowItems.forEach { (index, category) ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(categorySwatchColor(index)),
                        )
                        Text("${category.categoryText} ${category.amountText}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// 임시: 카테고리 구분색 대신 onSurface 하나의 진하기만 바꿔서 씀 (색 커스텀 금지 규칙 때문에 다색 대신 흑백 그라데이션으로 대체)
@Composable
private fun categorySwatchColor(index: Int): Color =
    MaterialTheme.colorScheme.onSurface.copy(alpha = (1f - index * 0.2f).coerceAtLeast(0.2f))

@Preview(showBackground = true)
@Composable
private fun ExpenseTripReportScreenPreview() {
    ExpenseTripReportScreen(
        state = ExpenseTripReportUiState(),
        onBack = {},
        onSettlementClick = {},
        onShareClick = {},
    )
}
