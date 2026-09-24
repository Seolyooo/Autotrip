# 경비 UI 진행 상황

> 기준 문서: `docs/expense-ui-plan.md`. 단계 끝날 때마다 체크 + 메모 갱신.
> 모든 단계 공통: 샘플 데이터만 사용, 화면마다 `@Preview` 1개, `./gradlew assembleDebug` 통과.

## 체크리스트

- [x] **U1. 샘플 데이터 + UiState**
  - `data/sample/ExpenseSampleData.kt` (오사카 3박 4일, 3명, 지출·정산 예시, 표시용 문자열 포함)
  - 화면별 `XxxUiState` 데이터 클래스 (`ui/expense/ExpenseUiState.kt`)
- [x] **U2. 연결 뼈대**
  - `navigation/ExpenseRoute` 정의, `navigation/ExpenseFlow` (백스택 `mutableStateListOf` + `when` + `BackHandler`)
  - 화면은 빈 자리표시로 두고 이동만 확인
  - `MainActivity`에 `ExpenseFlow` 진입 연결
- [x] **U3. 01 경비 홈** (`ExpenseHomeScreen.kt`)
  - 홈에서 나가는 이동 콜백 전부 (06, 07, 08, 04, 02, 03, 09, 11)
- [ ] **U4. 02 계획 금액** (`ExpensePlanListScreen.kt`) — 03 사전 결제는 06에 합침 (U5)
  - 02 '결제로 전환' → 06 (계획 항목 연결 채워서)
- [x] **U5. 06 지출 기록 · 07 영수증 확인** (`ExpenseRecordFormScreen.kt`, `ExpenseReceiptReviewScreen.kt`)
  - 06 카메라 → 07, 07 '확인하고 저장' → 뒤로 (06 거쳐 왔으면 06도 닫기)
  - 06 수정 모드 (샘플 값 채움)
  - [x] 06 지출 기록 화면 채움 (03 사전 결제 합침, `0306 지출 추가.png` 기준)
  - [x] 07 영수증 확인 화면 채움
- [ ] **U6. 08 현금 지갑 · 04 정산 · 12 초대** (`ExpenseCashWalletScreen.kt`, `ExpenseSettlementScreen.kt`, `ExpenseInviteScreen.kt`)
  - 04 '동행인 함께보기 초대' → 12
- [x] **U7. 09 소비 목록 · 10 정렬·필터** (`ExpenseListScreen.kt`, `ExpenseFilterSheet.kt`)
  - 필터 아이콘/정렬 칩 → 10 바텀시트, 항목 누름 → 06 수정 모드
- [ ] **U8. 11 여행 리포트 + 전체 점검** (`ExpenseTripReportScreen.kt`)
  - 11 정산 카드 → 04, 공유 → 12
  - plan 문서 2장 연결표 전 항목 + 시스템 뒤로 동작 확인

## 메모
- 기존 `ui.budget` 파일과 이름 겹침 → 새 코드는 `ui.expense`/`navigation`/`data.sample` 패키지 + `Expense` 접두어로 분리, `ui.budget` import 안 함. 06은 `ExpenseRecordFormScreen`으로 이름 바꿔서 겹침 없음
- U1·U2 (2026-09-23): 11개 화면 빈 뼈대 + `ExpenseFlow` 연결 완료. `HomeScreen`의 `onNavigateToBudget` → `showExpenseFlow`. `assembleDebug` 통과
  - 10은 `ExpenseRoute.FilterSheet`로 백스택에 올리고, `ExpenseFlow`가 09를 그린 위에 `ModalBottomSheet`를 띄움
  - 07 '확인하고 저장'은 07을 닫고 바로 아래가 06이면 06도 닫음
  - 임시: 백스택이 `remember`라 화면 회전 시 경비 홈으로 돌아감
- 기존 가계부 코드 정리 (2026-09-23): `ui/budget/ExpenseScreen.kt`, `ui/budget/CategoryScreen.kt`, MainActivity의 `showBudget`·`showBudgetDetail`·`showExpense` 분기 삭제. `assembleDebug` 통과
  - 논의 필요: `BudgetMainScreen`·`BudgetDetailScreen`·`BudgetModels`·`ui/budget/components`는 `navigation/AppNavHost.kt`가 import해서 남김 (AppNavHost는 어디서도 호출 안 됨)
- U3 (2026-09-23): 01 경비 홈을 와이어프레임 배치로 채움 (여행 헤더, 내 예산 카드, 현금 지갑·정산 카드, 바로가기 4개, 최근 기록, 카메라·기록 FAB). `assembleDebug` 통과
  - 샘플 추가: `SampleHomeSummary`(`homeSummary`), `trip.memberCountText`, `SampleExpense.categoryText`·`paidDateText`, 사전결제 `e6`(김해↔간사이 왕복), `recentExpenses`
  - `trip.periodText`에 요일 붙임, `e5` 제목을 '난바 호텔 3박'으로 바꿈 (11 리포트·09 목록에도 반영됨)
  - 빠진 요소: 상단 알림(종) 아이콘, 여행 이름 옆 전환 드롭다운 → 이동할 화면이 없어 보류
- U5-06 (2026-09-24): 06 지출 기록을 와이어프레임 배치로 채움 (알림 자동 입력 안내, 통화 드롭다운, 금액 입력, 원화 환산, 결제 수단 세그먼트, 카테고리 칩, 내용 입력, 결제한 사람, 나눌 사람 + 1인 금액, 하단 저장). `assembleDebug` 통과
  - 샘플 추가: `SampleRecordForm`, `recordFormDraft`(새 기록 = 알림 자동 입력, e1 값), e1~e4 수정 모드 폼, `currencyOptions`·`payMethodOptions`·`categoryOptions`
  - 환산 금액은 e1(35,838원)에 맞춤. 와이어프레임 반올림 값(35,840원·11,950원)과 다름
  - 임시: 금액·통화·나눌 사람을 바꿔도 환산·1인 금액 문구는 샘플 그대로 (계산은 기능 단계)
  - 빠진 요소: 없음. 멤버 원형 아바타(나·민·준)는 이름 FilterChip으로 대체
- U5-03+06 합침 (2026-09-24): 03 사전 결제와 06 지출 기록을 `ExpenseRecordFormScreen` 하나로 합침. `assembleDebug` 통과
  - 삭제: `ExpensePrepaidFormScreen.kt`, `ExpenseRoute.PrepaidForm`, `ExpensePrepaidFormUiState`. 01 '사전 결제'·02 '결제로 전환' → `RecordForm()`
  - 제목: 수정 모드면 '사전 결제 수정'/'지출 수정', 새 기록이면 `trip.isBeforeTrip`(임시 샘플 값)으로 '사전 결제 기록'/'지출 기록'
  - 논의 필요: 저장 시 사전 결제 분류는 오늘이 아니라 결제일 < 여행 시작일로 정할 것 (기능 단계)
  - 계획 항목 연결 칸: 위치 알림으로 들어오면 알림 문구, 아니면 계획 항목 드롭다운 ('연결 안 함' 포함)
  - 와이어프레임의 '내용'은 '메모'(선택 입력)로 바꿈. 항목명이 목록에 보이는 이름
  - 카테고리 8개(식비·교통·항공·숙박·관광·투어·입장권·쇼핑·기타), 처음엔 5개 + '…', 누르면 전부 펼침. 선택된 건 접혀도 보임
  - 샘플: `prepaidDraft`(e5 값), `duringTripDraft`(e1 값), e1~e6 수정 모드 폼, `planLinkOptions`, `splitModeOptions`
  - 임시: 결제일·이용일은 읽기 전용(TODO: DatePicker), 직접 입력 금액 칸은 빈 칸에서 시작
- U5-07 (2026-09-24): 07 영수증 확인을 와이어프레임 배치로 채움 (영수증 자리 회색 박스, 인식 결과 - 금액·날짜·결제수단(판단 근거)·가게 + 확인 체크, 카테고리 카드(미분류·확인 필요 → 칩 고르면 해제), 나눌 사람 요약 + 변경 시 멤버 칩 펼침, 하단 다시 찍기·확인하고 저장). `assembleDebug` 통과
  - 샘플 추가: `SampleReceiptScan`, `receiptScan`(e1 이치란 라멘 값 기준: ¥3,960, 2026.10.11 12:38, 현금, 一蘭 道頓堀店, 추천 카테고리 식비)
  - 카테고리 선택지는 06의 8개 전체가 아니라 07 전용 4개(식비·쇼핑·관광·기타)로 새로 둠. 추천 항목에 '(추천)' 표시
  - '다시 찍기'는 이동할 화면이 없어 onBack 재사용 (뒤로와 동일 동작)
  - 임시: 카테고리·나눌 사람을 바꿔도 저장 값은 그대로 버림 (계산·저장은 기능 단계)
  - 빠진 요소: 없음. 확인 체크·경고 아이콘은 Material 기본 Check/Warning 아이콘으로, 인식 결과 구분선은 실선(점선 대신)으로 대체
- U7 (2026-09-24): 09 소비 목록·10 정렬·필터를 와이어프레임 배치로 채움
  - 09: 여행 이름 드롭다운(자리만), 내 부담액/결제 금액 세그먼트 탭, 정렬·나누기·결제수단·결제자 칩 행(전부 onFilterClick), 합계 카드, 날짜별 묶음(그룹 헤더 + 항목 카드: 제목·시간, 금액 2~3줄, 결제수단·N빵·결제자·정산상태 칩)
  - 10: 정렬/날짜(읽기 전용 + 기준)/여행/나누기/결제한 사람/결제 수단/정산 상태 전부 FilterChip 단일 선택, 초기화 버튼은 진입 시 값으로 되돌림, 'N건 보기'는 샘플 문구 그대로
  - 샘플 추가: `SampleExpense`에 `timeText`·`myBurdenAmountText`·`myBurdenKrwText`·`totalAmountText`·`splitChipText`·`payerChipText`·`settlementChipText` 필드, `SampleExpenseDayGroup`·`SampleExpenseListSummary`, 09/10 공통 옵션 목록(`sortOptions`·`splitFilterOptions`·`payMethodFilterOptions`·`settlementFilterOptions`·`dateBasisOptions`·`tripFilterOptions`·`payerFilterOptions`)과 기본값
  - e1~e5에 카테고리·시간·내 부담액·칩 값 채움(e6은 여행 전 항공권이라 09 목록 묶음에서 뺌, 기존대로 홈 최근기록에서만 씀)
  - 임시: 09 상단 합계(23건·988,100원)와 날짜별 소계는 실제 있는 5건 합이 아니라 와이어프레임 값 그대로 둔 샘플. '결제 금액' 탭도 같은 금액 샘플을 그대로 씀(계산 없음)
  - 빠진 요소: 검색 아이콘(이동할 화면이 없어 보류), 주유패스 제목의 '2일' 수량 표기(기존 title 필드를 다른 화면들이 같이 써서 안 건드림)
  - 논의 필요: 09의 정렬/나누기/결제수단/결제자 칩은 전부 10 시트를 열기만 함(값으로 09에서 직접 필터링 안 함). 실제로 09에서 즉시 토글하게 할지, 10에서만 바꾸게 할지는 기능 단계에서 정할 것
