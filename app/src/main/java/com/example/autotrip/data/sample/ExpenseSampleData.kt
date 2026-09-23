package com.example.autotrip.data.sample

// 의도: UI 단계 전용 샘플 데이터. 화면에 보이는 값은 전부 여기서 가져옴
// 임시: 원화 환산·합계 같은 표시 값은 계산하지 않고 문자열로 미리 넣어둠. 계산은 기능 단계에서 할 것

data class SampleTrip(
    val title: String,
    val periodText: String,
    val memberNames: List<String>,
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
        periodText = "10/10 – 10/13",
        memberNames = listOf("나", "민지", "준호"),
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
            title = "난바 호텔",
            amountText = "540,000원",
            krwAmountText = "540,000원",
            payMethodText = "사전결제",
            splitText = "3명",
            payerName = "민지",
            isPrepaid = true,
        ),
    )

    val settlements = listOf(
        SampleSettlement(fromName = "민지", toName = "나", amountText = "120,000원", statusText = "받음"),
        SampleSettlement(fromName = "준호", toName = "나", amountText = "300,000원", statusText = "보냈어요"),
        SampleSettlement(fromName = "준호", toName = "민지", amountText = "180,000원", statusText = "대기"),
    )

    fun findExpense(id: String?): SampleExpense? = expenses.firstOrNull { it.id == id }
}
