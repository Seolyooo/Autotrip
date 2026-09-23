package com.example.autotrip.ui.expense

// 01 경비 홈

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 임시: 뼈대 단계라 상단바와 이동 버튼만 둠. U3에서 와이어프레임 배치로 교체할 것
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
                title = { Text(state.tripTitle) },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(state.periodText)
            Button(onClick = onRecordClick, modifier = Modifier.fillMaxWidth()) { Text("+ 기록") }
            Button(onClick = onCameraClick, modifier = Modifier.fillMaxWidth()) { Text("영수증 촬영") }
            Button(onClick = onCashWalletClick, modifier = Modifier.fillMaxWidth()) { Text("현금 지갑 · 환전 기록") }
            Button(onClick = onSettlementClick, modifier = Modifier.fillMaxWidth()) { Text("정산") }
            Button(onClick = onPlanListClick, modifier = Modifier.fillMaxWidth()) { Text("계획 금액") }
            Button(onClick = onPrepaidClick, modifier = Modifier.fillMaxWidth()) { Text("사전 결제") }
            Button(onClick = onExpenseListClick, modifier = Modifier.fillMaxWidth()) { Text("소비 목록 전체 보기") }
        }
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
