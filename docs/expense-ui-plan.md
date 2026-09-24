# 경비 UI 계획 — 화면 목록과 연결

> 지금 단계는 UI + 화면 연결만. 데이터는 샘플, 저장·계산 없음.
> 레이아웃 참고 이미지: `docs/wireframes/NN-이름.png` (캔버스 아트보드를 한 장씩 내보낸 것)

## 1. 화면 목록

| 번호 | 화면 | 파일 | 형태 | 이미지 |
|---|---|---|---|---|
| 01 | 경비 홈 | `ExpenseHomeScreen.kt` | 전체 화면 | 01-home.png |
| 02 | 계획 금액 | `ExpensePlanListScreen.kt` | 전체 화면 | 02-plan.png |
| 03 | (06에 합침) | — | — | 0306 지출 추가.png |
| 04 | 정산 | `ExpenseSettlementScreen.kt` | 전체 화면 | 04-settle.png |
| 06 | 지출 기록 (03 사전 결제 합침) | `ExpenseRecordFormScreen.kt` | 전체 화면 | 0306 지출 추가.png |
| 07 | 영수증 확인 | `ExpenseReceiptReviewScreen.kt` | 전체 화면 | 07-receipt.png |
| 08 | 현금 지갑 | `ExpenseCashWalletScreen.kt` | 전체 화면 | 08-cash.png |
| 09 | 소비 목록 | `ExpenseListScreen.kt` | 전체 화면 | 09-list.png |
| 10 | 정렬·필터 | `ExpenseFilterSheet.kt` | 09 위 바텀시트 | 10-filter.png |
| 11 | 여행 리포트 | `ExpenseTripReportScreen.kt` | 전체 화면 | 11-report.png |
| 12 | 함께보기 초대 | `ExpenseInviteScreen.kt` | 전체 화면 | 12-invite.png |

- 화면 파일 10개(03은 06에 합침)는 모두 `ui/expense/` (`com.example.autotrip.ui.expense`)

공통 파일

| 파일 | 위치 | 역할 |
|---|---|---|
| `ExpenseRoute.kt` | `navigation/` | 경비 화면 이름 정의 |
| `ExpenseFlow.kt` | `navigation/` | 백스택 + `when` + `BackHandler` |
| `ExpenseUiState.kt` | `ui/expense/` | 화면별 `XxxUiState` |
| `ExpenseSampleData.kt` | `data/sample/` | 샘플 데이터 |

- `navigation/`의 기존 파일(`Screen.kt`, `AppNavHost.kt`)과 `data/local`·`data/remote`·`data/repository`는 건드리지 않음

- 05 위치 알림은 시스템 알림이라 이번 단계에서 제외.
- 07은 카메라 없이 바로 확인 화면으로 이동. 영수증 자리는 회색 박스.

## 2. 화면 연결

| 출발 | 누르는 곳 | 도착 |
|---|---|---|
| 01 홈 | '+ 기록' 버튼 | 06 |
| 01 홈 | 카메라 버튼 | 07 |
| 01 홈 | 현금 지갑 카드 / '환전 기록' | 08 |
| 01 홈 | 정산 카드 | 04 |
| 01 홈 | '계획 금액' | 02 |
| 01 홈 | '사전 결제' | 06 (여행 전이면 '사전 결제 기록') |
| 01 홈 | '소비 목록' / '전체 보기' | 09 |
| 01 홈 | 상단 리포트 아이콘 (와이어프레임에 없음, 추가) | 11 |
| 02 계획 금액 | '결제로 전환' | 06 |
| 04 정산 | '동행인 함께보기 초대' | 12 |
| 06 지출 기록 | 카메라 아이콘 | 07 |
| 06 지출 기록 | 저장 / 닫기 | 뒤로 |
| 07 영수증 | '확인하고 저장' | 뒤로 (06을 거쳐 왔으면 06도 닫기) |
| 09 목록 | 필터 아이콘 / 정렬 칩 | 10 (바텀시트) |
| 09 목록 | 항목 누름 | 06 (수정 모드, 샘플 값 채워짐) |
| 11 리포트 | 정산 카드 | 04 |
| 11 리포트 | 공유 아이콘 | 12 |
| 모든 화면 | 뒤로 / 시스템 뒤로 | 이전 화면 |

## 3. 연결 방식
- `navigation/ExpenseRoute` (sealed class)로 화면 이름 정의
- `navigation/ExpenseFlow` 컴포저블 하나가 백스택(`mutableStateListOf<ExpenseRoute>`)을 들고 `when`으로 화면 표시
- `BackHandler`로 시스템 뒤로 처리
- `ExpenseFlow(onExit)`: 백스택에 홈만 남은 상태에서 뒤로 가면 `onExit` 호출
- `MainActivity`에는 `showExpenseFlow` 분기에서 `ExpenseFlow(onExit = ...)` 호출만 추가
- 의도: 나중에 NavController로 바꿔도 화면 함수는 그대로 쓰게 함

## 4. 샘플 데이터 (`data/sample/ExpenseSampleData.kt`)
와이어프레임 값 그대로 사용: 오사카 3박 4일, 10/10–10/13, 3명(나·민지·준호), 1인 예산 1,200,000원, 환율 9.05.
- 지출 예: 이치란 라멘 ¥3,960 현금 3명 / 고베규 저녁 ¥28,500 카드 3명(준호 결제) / 편의점 ¥860 현금 개인 / 주유패스 ¥16,500 카드 3명 / 난바 호텔 540,000원 사전결제(민지 결제)
- 정산 예: 민지→나 120,000원(받음) / 준호→나 300,000원(보냈어요) / 준호→민지 180,000원(대기)
- 원화 환산 같은 표시 값도 샘플에 미리 문자열로 넣어둠 (계산 로직은 다음 단계)

## 5. 이번 단계에서 하지 않는 것
DB·Room, ViewModel, 실제 계산(1/N, 환율, 정산), 카메라·OCR, 알림, 서버, 디자인 다듬기.
이 내용은 `expense-spec.md`에 정리돼 있고 기능 단계에서 사용.
