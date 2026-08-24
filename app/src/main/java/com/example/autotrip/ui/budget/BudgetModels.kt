package com.example.autotrip.ui.budget

import androidx.compose.ui.graphics.Color

/** 임시: 상세내역 화면에서 쓰는 개별 지출 항목 (스케치용, 실제 지출 트래커 연동 시 서버 모델로 교체) */
data class ExpenseItem(
    val category: String,
    val place: String,
    val amount: Long,
    val date: String = "8/24"
)

/** 메인화면 카테고리 breakdown 막대에 쓰는 집계 데이터 */
data class CategorySpend(
    val name: String,
    val amount: Long,
    val color: Color
)

fun List<CategorySpend>.totalAmount(): Long = sumOf { it.amount }
