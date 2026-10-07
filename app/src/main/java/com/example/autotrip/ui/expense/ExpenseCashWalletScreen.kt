package com.example.autotrip.ui.expense

// 08 현금 지갑

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleCashExchangeRecord
import com.example.autotrip.data.sample.SampleCashUsage

// 빠짐: 상단 '+' (환전 추가 단축) 아이콘 — 이동할 화면이 없어 보류
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCashWalletScreen(
    state: ExpenseCashWalletUiState,
    onBack: () -> Unit,
) {
    val wallet = state.wallet

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("현금 지갑 · ${wallet.currencyLabel}") },
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
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "지갑에 있어야 할 돈",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(wallet.balanceAmountText, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        wallet.balanceKrwText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    LinearProgressIndicator(
                        progress = { wallet.usedRatio },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("사용 ${wallet.usedAmountText}", style = MaterialTheme.typography.bodySmall)
                        Text("환전 ${wallet.exchangedAmountText}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // 임시: 아래 두 버튼은 자리만 (실제 잔액 맞추기 다이얼로그·환전 입력 폼은 다음 단계에서 연결)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("실제 잔액 맞추기") }
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("환전 추가") }
            }

            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("예) ${wallet.rebalanceExampleText}", style = MaterialTheme.typography.bodySmall)
                    Text("→ ${wallet.rebalanceResultText}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("환전 내역", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider()
                state.exchangeRecords.forEach { record -> CashExchangeRecordRow(record) }
                Text(
                    "수수료가 포함된 실제 환율로 자동 계산 · ATM 인출도 여기서",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("현금 사용", style = MaterialTheme.typography.titleMedium)
                    Text(
                        state.usageCountText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                state.usages.forEach { usage -> CashUsageRow(usage) }
            }
        }
    }
}

@Composable
private fun CashExchangeRecordRow(record: SampleCashExchangeRecord) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("${record.dateText} ${record.titleText}", style = MaterialTheme.typography.bodyLarge)
            Text(
                record.detailText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(record.amountText, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun CashUsageRow(usage: SampleCashUsage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                usage.dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(usage.title, style = MaterialTheme.typography.bodyLarge)
            usage.splitChipText?.let { AssistChip(onClick = {}, label = { Text(it) }) }
        }
        Text(usage.amountText, style = MaterialTheme.typography.titleMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseCashWalletScreenPreview() {
    ExpenseCashWalletScreen(
        state = ExpenseCashWalletUiState(),
        onBack = {},
    )
}
