package com.example.autotrip.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.home.components.TripLogCard

private data class TripLog(
    val dateRange: String,
    val title: String,
    val isCurrent: Boolean = false
)

// 임시: 목업 데이터 넣어둠. 실제 여행 기록은 API 붙일 때 교체할 것
private val sampleTripLogs = listOf(
    TripLog("2026.09.24 ~ 09.27", "도쿄 여행", isCurrent = true),
    TripLog("2025.12.20 ~ 12.23", "부산 여행"),
    TripLog("2025.07.02 ~ 07.06", "제주 여행")
)

/**
 * 홈의 "로그" 탭 내용. 지나간 여행과 현재 여행을 카드 목록으로 보여줌.
 */
@Composable
fun LogScreen(
    onLogClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleTripLogs) { log ->
            TripLogCard(
                dateRange = log.dateRange,
                title = log.title,
                isCurrent = log.isCurrent,
                onClick = { onLogClick(log.title) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun LogScreenPreview() {
    LogScreen()
}
