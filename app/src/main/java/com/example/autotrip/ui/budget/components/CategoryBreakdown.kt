package com.example.autotrip.ui.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.budget.CategorySpend
import com.example.autotrip.ui.budget.totalAmount
// 참고: formatWon()은 같은 패키지(ui.budget.components)의 BudgetDonutChart.kt에 있어서 import 안 함

/**
 * 카테고리별 지출을 세로 리스트 대신 "막대 하나 + 범례"로 압축해서 보여줌.
 * 의도: 카테고리 늘어나도 세로 공간 거의 안 먹게 하려고 이렇게 함.
 *
 * 비율이 작아서 막대 안에 이름이 안 들어가는 카테고리는, 실제 글자 폭을
 * 측정해서 막대 위/아래로 위치를 옮기는 방식도 가능하지만 지금 단계 대비 복잡도가
 * 꽤 올라가서, 우선은 비율(labelMinPercent) 기준으로 "안 들어갈 것 같으면 생략"
 * 방식으로 단순하게 처리함. 어차피 아래 범례에 이름·%·금액 다 있어서 정보 손실은 없음.
 */
@Composable
fun CategoryBreakdown(
    categories: List<CategorySpend>,
    modifier: Modifier = Modifier,
    labelMinPercent: Float = 0.15f
) {
    val total = categories.totalAmount().coerceAtLeast(1)

    Column(modifier = modifier.fillMaxWidth()) {
        // 의도: 막대를 두껍게 키워서 그 안에 카테고리명이 들어갈 자리를 만듦
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            categories.forEach { category ->
                val ratio = (category.amount.toFloat() / total.toFloat()).coerceAtLeast(0.001f)
                Box(
                    modifier = Modifier
                        .weight(ratio)
                        .fillMaxHeight()
                        .background(category.color),
                    contentAlignment = Alignment.Center
                ) {
                    // 의도: 비율이 labelMinPercent 이상일 때만 막대 안에 이름 표시.
                    // 너무 좁으면 글자가 잘려서 오히려 지저분해지므로 생략함 (범례에서 확인 가능).
                    if (ratio >= labelMinPercent) {
                        Text(
                            text = category.name,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 의도: 범례는 카테고리명 · % · 금액을 한 줄로 짧게 압축함 (막대 안에서 생략된 이름도 여기서 확인 가능)
        categories.forEach { category ->
            val percent = (category.amount.toFloat() / total.toFloat() * 100).toInt()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(category.color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = category.name, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = "$percent% · ${formatWon(category.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
