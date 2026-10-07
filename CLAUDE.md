# AutoTrip — Claude Code 작업 규칙

## 언어

모든 답변, 계획, 질문, 진행 보고는 한국어로 할 것

코드의 변수·함수·클래스 이름은 영어, 주석과 화면 텍스트는 한국어

## 프로젝트

* Android, Kotlin + Jetpack Compose
* 패키지: `com.example.autotrip` (옛 문서의 `com.syteam.autotripdiary`는 무시)
* 화면 전환: `MainActivity`의 `when` 방식. NavController가 연결돼 있다고 가정하지 말 것
* 새 라이브러리 추가 금지. 꼭 필요하면 먼저 물어볼 것

## 지금 단계: UI + 화면 연결만

* 경비 화면을 만들고 서로 연결하는 단계. 기준 문서: `docs/expense-ui-plan.md`
* **DB, Room, Repository, ViewModel, 서버 통신, 계산 로직은 만들지 말 것**
* 화면에 보이는 값은 전부 `data/sample/ExpenseSampleData.kt`의 샘플 데이터에서 가져올 것. 화면 코드에 숫자·이름 하드코딩 금지
* 칩 선택, 토글, 입력값 같은 화면 안 상태는 `remember`로 처리해도 됨
* 진행 상황: `docs/expense-ui-progress.md` 체크리스트를 단계마다 갱신

## 화면 코드 형태

* 경비 코드 위치
  * 화면·UiState: `com.example.autotrip.ui.expense`
  * `ExpenseRoute`, `ExpenseFlow`: `com.example.autotrip.navigation` (같은 폴더의 기존 파일은 건드리지 말 것)
  * `ExpenseSampleData`: `com.example.autotrip.data.sample` (`data/local`, `data/remote`, `data/repository`는 건드리지 말 것)
* 화면 함수는 `XxxScreen(state: XxxUiState, onBack: () -> Unit, 이동 콜백들...)` 형태. 화면 안에서 다른 화면을 직접 부르지 말 것
* 화면마다 `@Preview` 1개 (샘플 데이터 사용)
* Material3 기본 컴포넌트만. 색·폰트 커스텀 금지

## 작업 방식

* 요청한 범위 밖의 파일은 읽지도 고치지도 말 것. 프로젝트 전체 탐색 금지
* 기존 파일을 지우거나 이름 바꾸기 전에 물어볼 것
* 끝나면 파일 전체 출력 대신 **바뀐 파일 목록 + 파일마다 2줄 설명**만 보고
* 끝나면 `./gradlew assembleDebug`로 빌드 통과 확인

## 코드 주석

* 1인칭 설계 메모, 한국어 개조식(\~함/\~됨/\~할 것)
* 태그: `// 의도:` `// 임시:` `// TODO:` `// 논의 필요:`
* 팀원 이름 쓰지 말 것
* Compose에서 주석이 화면 텍스트로 들어가지 않게 주의

