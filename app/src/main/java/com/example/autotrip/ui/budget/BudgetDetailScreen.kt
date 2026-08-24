package com.example.autotrip.ui.budget

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.budget.components.formatWon

/**
 * 개별 지출 거래를 "찬찬히 들여다보는" 화면 — 여기는 스크롤 있어도 괜찮음
 * 의도: 메인화면은 빠르게 훑는 용도라 스크롤 없앴고,
 * 상세내역은 애초에 훑어보는 리스트라 스크롤 있는 게 자연스러워서 그대로 둠.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailScreen(
    onBackClick: () -> Unit = {}
) {
    // 목업 데이터 — 메인화면의 카테고리 합계와 맞춰둠 (식비 70,000 / 쇼핑 40,000 / 교통 20,000 / 숙박 83,000)
    val mockExpenses = listOf(
        ExpenseItem("식비", "점심 식당", 18_000, "8/22"),
        ExpenseItem("식비", "카페", 8_000, "8/23"),
        ExpenseItem("식비", "저녁 식당", 44_000, "8/23"),
        ExpenseItem("쇼핑", "기념품샵", 40_000, "8/23"),
        ExpenseItem("교통", "택시", 20_000, "8/24"),
        ExpenseItem("숙박", "게스트하우스", 83_000, "8/22")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상세내역") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            items(mockExpenses) { expense ->
                ExpenseRow(expense)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ExpenseRow(expense: ExpenseItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${expense.category} · ${expense.date}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = expense.place, style = MaterialTheme.typography.bodyLarge)
        }
        Text(
            text = formatWon(expense.amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun BudgetDetailScreenPreview() {
    BudgetDetailScreen()
}
