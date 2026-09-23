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
- [ ] **U3. 01 경비 홈** (`ExpenseHomeScreen.kt`)
  - 홈에서 나가는 이동 콜백 전부 (06, 07, 08, 04, 02, 03, 09, 11)
- [ ] **U4. 02 계획 금액 · 03 사전 결제** (`ExpensePlanListScreen.kt`, `ExpensePrepaidFormScreen.kt`)
  - 02 '결제로 전환' → 03, 03 저장/닫기 → 뒤로
- [ ] **U5. 06 지출 기록 · 07 영수증 확인** (`ExpenseRecordFormScreen.kt`, `ExpenseReceiptReviewScreen.kt`)
  - 06 카메라 → 07, 07 '확인하고 저장' → 뒤로 (06 거쳐 왔으면 06도 닫기)
  - 06 수정 모드 (샘플 값 채움)
- [ ] **U6. 08 현금 지갑 · 04 정산 · 12 초대** (`ExpenseCashWalletScreen.kt`, `ExpenseSettlementScreen.kt`, `ExpenseInviteScreen.kt`)
  - 04 '동행인 함께보기 초대' → 12
- [ ] **U7. 09 소비 목록 · 10 정렬·필터** (`ExpenseListScreen.kt`, `ExpenseFilterSheet.kt`)
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
