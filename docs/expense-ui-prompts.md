# 경비 UI — Claude Code 단계별 프롬프트

## 준비 (한 번만)
1. 프로젝트 루트에 `CLAUDE.md`, `docs/`에 `expense-ui-plan.md`를 넣는다
2. 캔버스 아트보드를 한 장씩 PNG로 내보내서 `docs/wireframes/`에 넣는다 (파일명은 plan 1장 표대로)
3. `expense-spec.md`는 **아직 프로젝트에 넣지 않는다** (넣으면 DB·계산까지 만들려고 함)
4. `git switch feature/susu` → Android Studio 터미널에서 `claude`

## 단계마다 반복
| 순서 | 할 일 |
|---|---|
| 시작 | `/clear` |
| 모델 | U0·U1은 `/model opus`, U2부터는 `/model sonnet` |
| 확인 | Android Studio에서 Preview 보기 → 필요하면 에뮬레이터 실행 → `git diff` 읽기 |
| 마무리 | 커밋 → 다음 단계 |

---

## U0 · 현황 파악 (코드 수정 없음)
```
CLAUDE.md와 docs/expense-ui-plan.md를 읽어.
그다음 MainActivity.kt만 열어봐. 다른 파일은 열지 마.
경비·예산 관련 파일이 이미 있는지는 파일 이름으로만 찾아봐 (Budget*, Expense*). 내용은 읽지 마.

알려줘:
1) MainActivity의 현재 화면 전환 구조 요약 (3줄 이내)
2) ExpenseFlow를 어디에 어떻게 연결할지
3) 이름이 겹치는 기존 파일이 있으면 목록

그리고 docs/expense-ui-progress.md를 만들어서 U1~U8 체크리스트를 적어줘.
코드는 수정하지 마.
```

## U1 · 뼈대: 샘플 데이터 + 연결 (내용은 빈 화면)
```
docs/expense-ui-plan.md의 2~4장대로 만들어줘.
- data/sample/ExpenseSampleData.kt (4장 샘플), ui/expense/ExpenseUiState.kt (화면별 UiState data class)
- navigation/ExpenseRoute + navigation/ExpenseFlow (백스택 + when + BackHandler)
- ui/expense에 11개 화면(1장 표의 파일명)을 빈 화면으로 생성: 상단바(제목 + 뒤로)와 2장 표의 이동 버튼만 있게
- MainActivity에 showExpenseFlow 분기 추가 (ExpenseFlow(onExit = ...))
먼저 계획만 보여주고, 내가 OK하면 작성해.
끝나면 빌드 확인하고 progress 체크.
```
→ 이 단계가 끝나면 앱에서 11개 화면을 전부 오갈 수 있어야 함. 여기서 연결을 확정하고 다음으로.

## U2~U8 · 화면 채우기 (한 세션에 한 묶음)
아래 틀에서 `[ ]` 부분만 바꿔서 쓴다.

```
[06 지출 기록] 화면 내용을 채워줘. U1에서 만든 ui/expense/[ExpenseRecordFormScreen.kt]를 수정하는 거야.
레이아웃은 docs/wireframes/[06-entry.png]를 참고해.
- 이미지 속 숫자·이름·금액은 샘플. 하드코딩하지 말고 UiState에서 가져와
- 이미지는 배치 순서와 들어갈 요소만 참고. 선·색·폰트는 따라 하지 마 (Material3 기본)
- 이동 콜백은 U1에서 정한 것 그대로 유지
- 샘플 값이 모자라면 data/sample/ExpenseSampleData.kt에 추가
끝나면 이미지에 있는데 빠뜨린 요소를 목록으로 알려주고, 빌드 확인 후 progress 체크.
```

| 단계 | 화면 | 파일 | 이미지 | 메모 |
|---|---|---|---|---|
| U2 | 01 경비 홈 | `ExpenseHomeScreen.kt` | 01-home.png | 상단 리포트 아이콘 추가 |
| U3 | 06 지출 기록 | `ExpenseRecordFormScreen.kt` | 06-entry.png | 현금/카드 토글, 1/N 대상 선택은 remember 상태 |
| U4 | 07 영수증 확인 | `ExpenseReceiptReviewScreen.kt` | 07-receipt.png | 영수증 자리는 회색 박스, '미분류' 확인 필요 표시 |
| U5 | 09 목록 + 10 필터 | `ExpenseListScreen.kt`, `ExpenseFilterSheet.kt` | 09-list.png, 10-filter.png | 10은 ModalBottomSheet. '내 부담액/결제 금액' 토글, 날짜 그룹 |
| U6 | 02 계획 금액 + 03 사전 결제 | `ExpensePlanListScreen.kt`, `ExpensePrepaidFormScreen.kt` | 02-plan.png, 03-prepaid.png | |
| U7 | 08 현금 지갑 + 04 정산 | `ExpenseCashWalletScreen.kt`, `ExpenseSettlementScreen.kt` | 08-cash.png, 04-settle.png | |
| U8 | 11 리포트 + 12 초대 | `ExpenseTripReportScreen.kt`, `ExpenseInviteScreen.kt` | 11-report.png, 12-invite.png | 카테고리 비중은 가로 막대 하나 |

화면 두 개를 묶는 단계(U5~U8)가 무거우면 한 화면씩 나눠서 진행.
