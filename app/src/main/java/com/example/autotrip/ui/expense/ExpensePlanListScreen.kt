package com.example.autotrip.ui.expense

// 02 계획 금액

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

// 임시: 뼈대 단계라 상단바와 이동 버튼만 둠. U4에서 와이어프레임 배치로 교체할 것
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensePlanListScreen(
    state: ExpensePlanListUiState,
    onBack: () -> Unit,
    onConvertToPrepaidClick: () -> Unit,
) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(state.tripTitle)
            Button(onClick = onConvertToPrepaidClick, modifier = Modifier.fillMaxWidth()) { Text("결제로 전환") }
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
