package com.example.autotrip.ui.expense

// 09 소비 목록

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 임시: 뼈대 단계라 상단바와 이동용 칩·버튼만 둠. U7에서 와이어프레임 배치로 교체할 것
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListUiState,
    onBack: () -> Unit,
    onFilterClick: () -> Unit,
    onExpenseClick: (expenseId: String) -> Unit,
) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AssistChip(onClick = onFilterClick, label = { Text("정렬") })
            state.expenses.forEach { expense ->
                // 임시: 항목 카드 대신 버튼. 누르면 06 수정 모드로 이동
                OutlinedButton(
                    onClick = { onExpenseClick(expense.id) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(expense.title)
                }
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
