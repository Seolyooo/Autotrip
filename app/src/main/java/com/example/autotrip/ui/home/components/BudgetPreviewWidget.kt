package com.example.autotrip.ui.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 메인화면 하단의 "예산 미리보기 / 지금까지 쓴 금액 / 남은 금액" 요약 위젯.
 * 임시: 지금은 표시용 목업 값이고, 실제 숫자는 지출 트래커 연동되면 채워질 것.
 */
@Composable
fun BudgetPreviewWidget(
    totalBudgetLabel: String,
    spentSoFarLabel: String,
    remainingLabel: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        BulletLine(text = "예산 미리보기: $totalBudgetLabel")
        Spacer(modifier = Modifier.height(6.dp))
        BulletLine(text = "내가 지금까지 쓴 금액정도: $spentSoFarLabel")
        Spacer(modifier = Modifier.height(6.dp))
        BulletLine(text = "남은금액: $remainingLabel", indent = true)
    }
}

@Composable
private fun BulletLine(text: String, indent: Boolean = false) {
    Row(modifier = Modifier.padding(start = if (indent) 16.dp else 0.dp)) {
        Text(text = "• ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
