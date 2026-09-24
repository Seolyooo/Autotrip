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
    // 임시: 오늘이 여행 시작 전인지. 날짜 비교 대신 샘플 값으로 둠 (홈 D-17 기준 true). 여행 중 화면 보려면 false로 바꿀 것
    val isBeforeTrip: Boolean,
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
    // 09 소비 목록 전용: amountText·krwAmountText는 전체 금액, 아래는 '내 부담액' 탭 표시 값
    val timeText: String = "",
    val myBurdenAmountText: String = amountText,
    val myBurdenKrwText: String? = null,
    val totalAmountText: String? = null,
    val splitChipText: String? = null,
    val payerChipText: String? = null,
    val settlementChipText: String? = null,
)

// 의도: 09 소비 목록의 날짜 묶음 하나. 부담 합계는 계산하지 않고 문자열로 미리 넣어둠
data class SampleExpenseDayGroup(
    val dateLabel: String,
    val myBurdenSubtotalText: String,
    val expenses: List<SampleExpense>,
)

// 의도: 09 상단 합계 카드 값
data class SampleExpenseListSummary(
    val totalCountText: String,
    val totalAmountText: String,
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

// 의도: 07 영수증 확인 화면에 채울 인식 결과 값. OCR 없이 미리 넣어둔 샘플
data class SampleReceiptScan(
    val photoPlaceholderText: String,
    val amountText: String,
    val dateTimeText: String,
    val payMethodText: String,
    val payMethodReasonText: String,
    val storeText: String,
    val categoryDefaultText: String,
    val recommendedCategory: String,
    val categoryOptions: List<String>,
    val categoryHintText: String,
    val splitSummaryText: String,
)

// 의도: 03+06 지출 기록 폼에 채워 넣을 값. 환산·1인 금액은 계산 전이라 문자열로 미리 넣어둠
// 의도: autoFillText가 있으면 계획 항목 연결 칸 자리에 위치 알림 문구를 보여줌
data class SampleRecordForm(
    val autoFillText: String?,
    val planLinkText: String?,
    val title: String,
    val currencyText: String,
    val amountInputText: String,
    val krwHintText: String?,
    val payMethodText: String,
    val categoryText: String,
    val paidDateText: String,
    val useDateText: String,
    val payerName: String,
    val splitMemberNames: List<String>,
    val perPersonText: String,
    val perPersonKrwText: String?,
    val splitModeText: String,
    val isSettlementExcluded: Boolean,
    val memoText: String,
)

object ExpenseSampleData {

    val trip = SampleTrip(
        title = "오사카 3박 4일",
        periodText = "10/10(금) – 10/13(월)",
        memberNames = listOf("나", "민지", "준호"),
        memberCountText = "3명",
        budgetPerPersonText = "1,200,000원",
        exchangeRateText = "¥1 = 9.05원",
        isBeforeTrip = true,
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
            categoryText = "식비",
            timeText = "12:40",
            myBurdenAmountText = "¥1,320",
            myBurdenKrwText = "≈ 11,950원",
            totalAmountText = "전체 ¥3,960",
            splitChipText = "N빵 3",
            payerChipText = "나 결제",
        ),
        SampleExpense(
            id = "e2",
            title = "고베규 저녁",
            amountText = "¥28,500",
            krwAmountText = "257,925원",
            payMethodText = "카드",
            splitText = "3명",
            payerName = "준호",
            categoryText = "식비",
            timeText = "19:10",
            myBurdenAmountText = "¥9,500",
            myBurdenKrwText = "≈ 86,000원",
            totalAmountText = "전체 ¥28,500",
            splitChipText = "N빵 3",
            payerChipText = "준호 결제",
            settlementChipText = "정산 대기",
        ),
        SampleExpense(
            id = "e3",
            title = "편의점",
            amountText = "¥860",
            krwAmountText = "7,783원",
            payMethodText = "현금",
            splitText = "개인",
            payerName = "나",
            categoryText = "쇼핑",
            timeText = "15:02",
            myBurdenAmountText = "¥860",
            myBurdenKrwText = "≈ 7,780원",
            splitChipText = "개인",
        ),
        SampleExpense(
            id = "e4",
            title = "주유패스",
            amountText = "¥16,500",
            krwAmountText = "149,325원",
            payMethodText = "카드",
            splitText = "3명",
            payerName = "나",
            categoryText = "교통",
            timeText = "10:15",
            myBurdenAmountText = "¥5,500",
            myBurdenKrwText = "≈ 49,780원",
            totalAmountText = "전체 ¥16,500",
            splitChipText = "N빵 3",
            payerChipText = "나 결제",
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
            timeText = "이용일",
            myBurdenAmountText = "180,000원",
            totalAmountText = "전체 540,000원",
            payerChipText = "민지 결제",
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

    // 09 소비 목록: 날짜별 묶음(최신순 · 이용일 기준). e6은 여행 전 항공권이라 목록에서 뺌
    val expenseDayGroups = listOf(
        SampleExpenseDayGroup(
            dateLabel = "10/11 (토)",
            myBurdenSubtotalText = "내 부담 105,730원",
            expenses = listOf("e2", "e3", "e1").mapNotNull { findExpense(it) },
        ),
        SampleExpenseDayGroup(
            dateLabel = "10/10 (금)",
            myBurdenSubtotalText = "내 부담 229,780원",
            expenses = listOf("e4", "e5").mapNotNull { findExpense(it) },
        ),
    )

    // 임시: 위 두 날짜 묶음(5건)만 실제로 있고, 합계는 와이어프레임 값(23건)을 그대로 넣어둠
    val expenseListSummary = SampleExpenseListSummary(
        totalCountText = "23건",
        totalAmountText = "988,100원",
    )

    // 09/10 공통: 정렬·필터 옵션과 기본 선택값
    val sortOptions = listOf("최신순", "과거순", "금액 큰 순", "금액 작은 순")
    val splitFilterOptions = listOf("전체", "N빵만", "개인만")
    val payMethodFilterOptions = listOf("전체", "현금", "카드")
    val settlementFilterOptions = listOf("전체", "미정산만")
    val dateBasisOptions = listOf("이용일 기준", "결제일 기준")
    val tripFilterOptions = listOf(trip.title, "전체 여행")
    val payerFilterOptions = listOf("전체") + trip.memberNames

    val defaultSortOption = sortOptions.first()
    val defaultSplitFilter = "N빵만"
    val defaultDateFromText = "2026.10.10"
    val defaultDateToText = "2026.10.13"
    // 임시: '14건 보기'는 위 필터 기본값에 맞춘 샘플 문구. 선택을 바꿔도 계산하지 않음 (기능 단계에서 처리)
    val filterResultCountText = "14건 보기"

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

    // 의도: 07 영수증 확인 와이어프레임 값(e1 이치란 라멘 기준)으로 채움
    val receiptScan = SampleReceiptScan(
        photoPlaceholderText = "촬영한 영수증",
        amountText = "¥3,960",
        dateTimeText = "2026.10.11 12:38",
        payMethodText = "현금",
        payMethodReasonText = "영수증의 '現金' 표기로 판단",
        storeText = "一蘭 道頓堀店",
        categoryDefaultText = "미분류",
        recommendedCategory = "식비",
        categoryOptions = listOf("식비", "쇼핑", "관광", "기타"),
        categoryHintText = "가게 이름으로 추천해요. 하나 고르면 끝.",
        splitSummaryText = "최근 설정 · 3명 균등",
    )

    // 지출 기록(03+06) 선택지
    val currencyOptions = listOf("JPY ¥", "KRW ₩")
    val payMethodOptions = listOf("현금", "카드")
    val categoryOptions = listOf("식비", "교통", "항공", "숙박", "관광", "투어·입장권", "쇼핑", "기타")
    val splitModeOptions = listOf("균등 분할", "직접 입력")
    // 임시: 02 계획 금액 샘플이 아직 없어 연결 후보를 문자열로만 둠. U4에서 02 샘플과 맞출 것
    val planLinkOptions = listOf(
        "숙박 · 난바 3박 (조사 600,000원)",
        "항공 · 김해↔간사이 왕복 (조사 960,000원)",
        "관광 · 주유패스 (조사 150,000원)",
    )

    private val allMembers = listOf("나", "민지", "준호")

    // 의도: 여행 전 새 기록 = 사전 결제. 03 와이어프레임 값(e5 난바 호텔)으로 채움
    val prepaidDraft = SampleRecordForm(
        autoFillText = null,
        planLinkText = "숙박 · 난바 3박 (조사 600,000원)",
        title = "난바 호텔 3박",
        currencyText = "KRW ₩",
        amountInputText = "540,000",
        krwHintText = null,
        payMethodText = "카드",
        categoryText = "숙박",
        paidDateText = "2026.09.02",
        useDateText = "10/10 – 10/13",
        payerName = "민지",
        splitMemberNames = allMembers,
        perPersonText = "1인 180,000원",
        perPersonKrwText = null,
        splitModeText = "균등 분할",
        isSettlementExcluded = false,
        memoText = "",
    )

    // 의도: 여행 중 새 기록 = 위치 알림에서 들어온 상황. 06 와이어프레임 값(e1 이치란 라멘)으로 채움
    // 임시: 환산 금액은 e1 값(¥3,960 × 9.05)에 맞춤. 와이어프레임의 반올림 값(35,840원)과 다름
    val duringTripDraft = SampleRecordForm(
        autoFillText = "도톤보리 · 10/11(토) 12:40 · 알림에서 자동 입력",
        planLinkText = null,
        title = "이치란 라멘",
        currencyText = "JPY ¥",
        amountInputText = "3,960",
        krwHintText = "≈ 35,838원 · 환율 9.05 (현금 지갑 환전 환율)",
        payMethodText = "현금",
        categoryText = "식비",
        paidDateText = "2026.10.11",
        useDateText = "10/11",
        payerName = "나",
        splitMemberNames = allMembers,
        perPersonText = "1인 ¥1,320",
        perPersonKrwText = "≈ 11,946원",
        splitModeText = "균등 분할",
        isSettlementExcluded = false,
        memoText = "",
    )

    // 의도: 09에서 항목 눌러 들어오는 수정 모드 값. 알림 자동 입력 문구는 없음
    private val recordForms = mapOf(
        "e1" to duringTripDraft.copy(autoFillText = null),
        "e2" to duringTripDraft.copy(
            autoFillText = null,
            title = "고베규 저녁",
            amountInputText = "28,500",
            krwHintText = "≈ 257,925원 · 환율 9.05",
            payMethodText = "카드",
            payerName = "준호",
            perPersonText = "1인 ¥9,500",
            perPersonKrwText = "≈ 85,975원",
            memoText = "산노미야 역 근처",
        ),
        "e3" to duringTripDraft.copy(
            autoFillText = null,
            title = "편의점",
            amountInputText = "860",
            krwHintText = "≈ 7,783원 · 환율 9.05 (현금 지갑 환전 환율)",
            categoryText = "기타",
            paidDateText = "2026.10.12",
            useDateText = "10/12",
            splitMemberNames = listOf("나"),
            perPersonText = "1인 ¥860",
            perPersonKrwText = "≈ 7,783원",
        ),
        "e4" to duringTripDraft.copy(
            autoFillText = null,
            planLinkText = "관광 · 주유패스 (조사 150,000원)",
            title = "주유패스",
            amountInputText = "16,500",
            krwHintText = "≈ 149,325원 · 환율 9.05",
            payMethodText = "카드",
            categoryText = "투어·입장권",
            paidDateText = "2026.10.10",
            useDateText = "10/10 – 10/11",
            perPersonText = "1인 ¥5,500",
            perPersonKrwText = "≈ 49,775원",
        ),
        "e5" to prepaidDraft,
        "e6" to prepaidDraft.copy(
            planLinkText = "항공 · 김해↔간사이 왕복 (조사 960,000원)",
            title = "김해↔간사이 왕복",
            amountInputText = "900,000",
            categoryText = "항공",
            paidDateText = "2026.08.20",
            payerName = "나",
            perPersonText = "1인 300,000원",
        ),
    )

    fun findExpense(id: String?): SampleExpense? = expenses.firstOrNull { it.id == id }

    fun findRecordForm(id: String?): SampleRecordForm? = recordForms[id]
}
