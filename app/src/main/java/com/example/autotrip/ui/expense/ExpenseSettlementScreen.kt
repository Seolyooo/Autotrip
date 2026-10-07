package com.example.autotrip.ui.expense

// 04 정산

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleSettlement
import com.example.autotrip.data.sample.SampleSettlementBasisItem

// 빠짐: 상단 공유 아이콘 — 이동할 화면이 없어 보류 (동행인 초대는 하단 버튼으로 이미 있음)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseSettlementScreen(
    state: ExpenseSettlementUiState,
    onBack: () -> Unit,
    onInviteClick: () -> Unit,
) {
    // 의도: '정산 확인 안 함' 토글은 화면 안 상태. 저장하지 않음
    var isSettlementHidden by remember(state) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("정산 · ${state.tripTitle}") },
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
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "받을 돈",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(state.summary.receivableAmountText, style = MaterialTheme.typography.headlineMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "보낼 돈",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(state.summary.payableAmountText, style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                    LinearProgressIndicator(
                        progress = { state.summary.progressRatio },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        state.summary.completionText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("이 여행은 정산 확인 안 함", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = isSettlementHidden, onCheckedChange = { isSettlementHidden = it })
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("누가 누구에게", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "송금 최소화",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                state.settlements.forEach { settlement -> SettlementCard(settlement) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("근거 항목", style = MaterialTheme.typography.titleMedium)
                    Text(
                        state.basisExcludedCountText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                state.basisItems.forEach { item -> SettlementBasisRow(item) }
            }

            Button(onClick = onInviteClick, modifier = Modifier.fillMaxWidth()) { Text("동행인 함께보기 초대") }
        }
    }
}

// 의도: 사람 아바타 대신(U5 결정과 동일) 이름 앞글자 칩으로 표시
@Composable
private fun SettlementCard(settlement: SampleSettlement) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MemberInitialBadge(settlement.fromInitial)
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    MemberInitialBadge(settlement.toInitial)
                }
                Text(settlement.amountText, style = MaterialTheme.typography.titleLarge)
            }
            when {
                settlement.doneBadgeText != null -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(settlement.doneBadgeText) },
                        leadingIcon = { Icon(Icons.Filled.Check, contentDescription = null) },
                    )
                }

                settlement.actionButtonText != null -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    settlement.waitingText?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    // 임시: '받았어요' 확인 처리는 다음 단계에서 연결 (지금은 자리만)
                    Button(onClick = {}) { Text(settlement.actionButtonText) }
                }

                settlement.waitingText != null -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        settlement.waitingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// 임시: onClick 없이 보여주기만 함. 나중에 멤버 정보로 연결하면 터치 영역(48dp)도 다시 볼 것
@Composable
private fun MemberInitialBadge(initial: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            initial,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun SettlementBasisRow(item: SampleSettlementBasisItem) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(item.titleText, style = MaterialTheme.typography.bodyLarge)
            Text(
                item.payerAndCountText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(item.perPersonAmountText, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseSettlementScreenPreview() {
    ExpenseSettlementScreen(
        state = ExpenseSettlementUiState(),
        onBack = {},
        onInviteClick = {},
    )
}
