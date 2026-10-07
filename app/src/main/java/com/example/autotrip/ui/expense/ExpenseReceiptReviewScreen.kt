package com.example.autotrip.ui.expense

// 07 영수증 확인

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpenseReceiptReviewScreen(
    state: ExpenseReceiptReviewUiState,
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val scan = state.scan
    // 의도: 카테고리는 고를 때까지 미분류로 두고 확인 필요 표시. 나눌 사람은 변경 눌렀을 때만 목록을 펼침
    var category by remember(state) { mutableStateOf<String?>(null) }
    var splitChangeExpanded by remember(state) { mutableStateOf(false) }
    var splitMembers by remember(state) { mutableStateOf(state.memberNames.toSet()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("영수증 확인") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
        bottomBar = {
            // 의도: 시스템 내비게이션 바에 버튼이 가려지지 않게 navigationBarsPadding 적용
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("다시 찍기")
                }
                Button(onClick = onConfirmClick, modifier = Modifier.weight(1.5f)) {
                    Text("확인하고 저장")
                }
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
            // 촬영한 영수증 자리 (카메라 없이 회색 박스로 대체)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    scan.photoPlaceholderText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("인식 결과", style = MaterialTheme.typography.titleMedium)
                Text(
                    "확인 후 저장",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalDivider()

            ReceiptResultRow(label = "금액", value = scan.amountText)
            HorizontalDivider()
            ReceiptResultRow(label = "날짜", value = scan.dateTimeText)
            HorizontalDivider()
            ReceiptResultRow(
                label = "결제수단",
                value = scan.payMethodText,
                supportingText = scan.payMethodReasonText,
            )
            HorizontalDivider()
            ReceiptResultRow(label = "가게", value = scan.storeText)

            // 카테고리: 고를 때까지 미분류 + 확인 필요 표시
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "카테고리",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(category ?: scan.categoryDefaultText, style = MaterialTheme.typography.titleMedium)
                        }
                        if (category == null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                )
                                Text(
                                    "확인 필요",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        } else {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        scan.categoryHintText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        scan.categoryOptions.forEach { option ->
                            val label = if (option == scan.recommendedCategory) "$option (추천)" else option
                            FilterChip(
                                selected = category == option,
                                onClick = { category = option },
                                label = { Text(label) },
                            )
                        }
                    }
                }
            }

            // 나눌 사람: 변경 누르면 멤버 선택 칩을 펼침
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "나눌 사람",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(scan.splitSummaryText, style = MaterialTheme.typography.titleMedium)
                    }
                    OutlinedButton(onClick = { splitChangeExpanded = !splitChangeExpanded }) {
                        Text("변경")
                    }
                }
                if (splitChangeExpanded) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.memberNames.forEach { name ->
                            val selected = name in splitMembers
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    splitMembers = if (selected) splitMembers - name else splitMembers + name
                                },
                                label = { Text(name) },
                            )
                        }
                    }
                }
            }
        }
    }
}

// 의도: 금액·날짜·결제수단·가게처럼 '라벨 + 인식값 + 확인 체크' 형태로 반복되는 줄
@Composable
private fun ReceiptResultRow(
    label: String,
    value: String,
    supportingText: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(88.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(value, style = MaterialTheme.typography.titleMedium)
            supportingText?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseReceiptReviewScreenPreview() {
    ExpenseReceiptReviewScreen(
        state = ExpenseReceiptReviewUiState(),
        onBack = {},
        onConfirmClick = {},
    )
}
