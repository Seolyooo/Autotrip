package com.example.autotrip.data.sample

// 의도: UI 단계 전용 샘플 데이터. 화면에 보이는 값은 전부 여기서 가져옴
// 임시: 원화 환산·합계 같은 표시 값은 계산하지 않고 문자열로 미리 넣어둠. 계산은 기능 단계에서 할 것

data class SampleTrip(
    val title: String,
    val periodText: String,
    val memberNames: List<String>,
    val memberCountText: String,
    val budgetPerPersonText: String,
    val exchangeRateText: String,
)

data class SampleExpense(
    val id: String,
    val title: String,
    val amountText: String,
    val krwAmountText: String,
    val payMethodText: String,
    val splitText: String,
    val payerName: String,
    val isPrepaid: Boolean = false,
    val categoryText: String = "",
    val paidDateText: String = "",
)

// 의도: 01 경비 홈 카드에 보이는 요약 값. 합계·비율은 계산 전이라 미리 넣어둠
data class SampleHomeSummary(
    val dDayText: String,
    val budgetUsedRatio: Float,
    val prepaidTotalText: String,
    val remainingBudgetText: String,
    val planTotalText: String,
    val planStatusText: String,
    val cashBalanceText: String,
    val cashKrwText: String,
    val settlementAmountText: String,
    val settlementSummaryText: String,
)

data class SampleSettlement(
    val fromName: String,
    val toName: String,
    val amountText: String,
    val statusText: String,
)

object ExpenseSampleData {

    val trip = SampleTrip(
        title = "오사카 3박 4일",
        periodText = "10/10(금) – 10/13(월)",
        memberNames = listOf("나", "민지", "준호"),
        memberCountText = "3명",
        budgetPerPersonText = "1,200,000원",
        exchangeRateText = "¥1 = 9.05원",
    )

    val expenses = listOf(
        SampleExpense(
            id = "e1",
            title = "이치란 라멘",
            amountText = "¥3,960",
            krwAmountText = "35,838원",
            payMethodText = "현금",
            splitText = "3명",
            payerName = "나",
        ),
        SampleExpense(
            id = "e2",
            title = "고베규 저녁",
            amountText = "¥28,500",
            krwAmountText = "257,925원",
            payMethodText = "카드",
            splitText = "3명",
            payerName = "준호",
        ),
        SampleExpense(
            id = "e3",
            title = "편의점",
            amountText = "¥860",
            krwAmountText = "7,783원",
            payMethodText = "현금",
            splitText = "개인",
            payerName = "나",
        ),
        SampleExpense(
            id = "e4",
            title = "주유패스",
            amountText = "¥16,500",
            krwAmountText = "149,325원",
            payMethodText = "카드",
            splitText = "3명",
            payerName = "나",
        ),
        SampleExpense(
            id = "e5",
            title = "난바 호텔 3박",
            amountText = "540,000원",
            krwAmountText = "540,000원",
            payMethodText = "사전결제",
            splitText = "3명",
            payerName = "민지",
            isPrepaid = true,
            categoryText = "숙박",
            paidDateText = "9/02",
        ),
        SampleExpense(
            id = "e6",
            title = "김해↔간사이 왕복",
            amountText = "900,000원",
            krwAmountText = "900,000원",
            payMethodText = "사전결제",
            splitText = "3명",
            payerName = "나",
            isPrepaid = true,
            categoryText = "교통",
            paidDateText = "8/20",
        ),
    )

    // 임시: 여행 전(D-17) 기준이라 최근 기록은 사전 결제 두 건만 둠. 정렬 기준은 기능 단계에서 정할 것
    val recentExpenses = listOf("e5", "e6").mapNotNull { findExpense(it) }

    // 의도: 사전 결제 480,000원 = 호텔·항공 3명 분할 중 내 몫(180,000 + 300,000)으로 맞춤
    val homeSummary = SampleHomeSummary(
        dDayText = "D-17",
        budgetUsedRatio = 0.4f,
        prepaidTotalText = "480,000원",
        remainingBudgetText = "720,000원",
        planTotalText = "≈917,000원",
        planStatusText = "예산 안에 들어와요",
        cashBalanceText = "¥50,000",
        cashKrwText = "452,500원",
        settlementAmountText = "420,000원",
        settlementSummaryText = "받을 돈 · 2건 대기",
    )

    val settlements = listOf(
        SampleSettlement(fromName = "민지", toName = "나", amountText = "120,000원", statusText = "받음"),
        SampleSettlement(fromName = "준호", toName = "나", amountText = "300,000원", statusText = "보냈어요"),
        SampleSettlement(fromName = "준호", toName = "민지", amountText = "180,000원", statusText = "대기"),
    )

    fun findExpense(id: String?): SampleExpense? = expenses.firstOrNull { it.id == id }
}
