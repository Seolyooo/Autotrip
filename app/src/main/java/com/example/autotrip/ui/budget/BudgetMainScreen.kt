package com.example.autotrip.ui.budget
// Epic: 예산 관리
// TODO: 화면 구현 예정

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.budget.components.BudgetDonutChart
import com.example.autotrip.ui.budget.components.CategoryBreakdown
import com.example.autotrip.ui.theme.CategoryFood
import com.example.autotrip.ui.theme.CategoryLodging
import com.example.autotrip.ui.theme.CategoryShopping
import com.example.autotrip.ui.theme.CategoryTransport

/**
 * 가계부 메인화면 — "빠르게 파악용" 한 화면. 스크롤 없이 한 화면에 다 담는 게 목표.
 *
 * 의도:
 * - 상단 도넛 그래프 하나에 백분율/사용액/총예산만 (일별 추이 등 없음)
 * - 카테고리별 지출은 리스트가 아니라 막대+범례로 압축 (스크롤 없이 다 보이게)
 * - 영수증 인식(OCR) 추가 버튼을 주 동작으로 배치 (지금은 클릭 이벤트만, 실제 인식은 나중에)
 * - 개별 거래 내역을 "보는" 화면은 스크롤 있어도 되니 BudgetDetailScreen으로 분리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetMainScreen(
    onBackClick: () -> Unit = {},
    onScanReceiptClick: () -> Unit = {},
    onManualAddClick: () -> Unit = {},
    onSeeDetailClick: () -> Unit = {}
) {
    // 임시: 목업 데이터로 넣어둠. 실제 지출 트래커 연동되면 서버 응답으로 교체할 것
    val totalBudget = 500_000L
    val categories = listOf(
        CategorySpend("식비", 70_000L, CategoryFood),
        CategorySpend("쇼핑", 40_000L, CategoryShopping),
        CategorySpend("교통", 20_000L, CategoryTransport),
        CategorySpend("숙박", 83_000L, CategoryLodging)
    )
    val spent = categories.totalAmount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("가계부") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                // 의도: 상단은 도넛 그래프 하나로 백분율/사용액/총예산 다 보여줌
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    BudgetDonutChart(spent = spent, totalBudget = totalBudget)
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 의도: 카테고리별 지출은 막대+범례로 압축해서 스크롤 없이 한눈에 보이게 함
                Text(
                    text = "카테고리별 지출",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                CategoryBreakdown(categories = categories)

                Spacer(modifier = Modifier.height(16.dp))

                // 의도: 상세내역(개별 거래 보기)은 여기서만 스크롤 화면으로 이동하게 함
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "상세내역 보기",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onSeeDetailClick) {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "상세내역으로 이동",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 의도: 지출 추가는 영수증 자동 인식을 주 동작으로, 직접 입력은 보조로 둠
            Column(modifier = Modifier.padding(bottom = 20.dp)) {
                Button(
                    onClick = onScanReceiptClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("영수증으로 추가")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onManualAddClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("직접 입력")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun BudgetMainScreenPreview() {
    BudgetMainScreen()
}
