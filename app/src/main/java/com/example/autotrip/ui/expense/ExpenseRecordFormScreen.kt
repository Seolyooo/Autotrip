package com.example.autotrip.ui.expense

// 03 사전 결제 기록 + 06 지출 기록 (합친 화면)

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 의도: 카테고리는 접힌 상태에서 앞쪽 몇 개만 보여주고 '…'으로 펼침 (폰 폭 기준 약 1~2줄)
private const val COLLAPSED_CATEGORY_COUNT = 5

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExpenseRecordFormScreen(
    state: ExpenseRecordFormUiState,
    onBack: () -> Unit,
    onCameraClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val form = state.form
    // 의도: 입력·선택 값은 화면 안 상태로만 둠. 저장은 이동만 하고 값은 버림
    var planLink by remember(state) { mutableStateOf(form.planLinkText) }
    var planMenuOpen by remember { mutableStateOf(false) }
    var titleInput by remember(state) { mutableStateOf(form.title) }
    var currency by remember(state) { mutableStateOf(form.currencyText) }
    var currencyMenuOpen by remember { mutableStateOf(false) }
    var amountInput by remember(state) { mutableStateOf(form.amountInputText) }
    var payMethod by remember(state) { mutableStateOf(form.payMethodText) }
    var category by remember(state) { mutableStateOf(form.categoryText) }
    var categoriesExpanded by remember { mutableStateOf(false) }
    var payerName by remember(state) { mutableStateOf(form.payerName) }
    var splitMembers by remember(state) { mutableStateOf(form.splitMemberNames.toSet()) }
    var splitMode by remember(state) { mutableStateOf(form.splitModeText) }
    val directAmounts = remember(state) { mutableStateMapOf<String, String>() }
    var settlementExcluded by remember(state) { mutableStateOf(form.isSettlementExcluded) }
    var memoInput by remember(state) { mutableStateOf(form.memoText) }

    // 의도: 균등 분할이 선택지 첫 번째. 1인 금액은 균등 분할일 때만 보여줌
    val isEvenSplit = splitMode == state.splitModeOptions.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.titleText) },
                navigationIcon = {
                    // 의도: 입력 폼이라 뒤로 대신 닫기 아이콘. 동작은 뒤로와 같음
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, contentDescription = "닫기")
                    }
                },
                actions = {
                    IconButton(onClick = onCameraClick) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = "영수증 촬영")
                    }
                },
            )
        },
        bottomBar = {
            // 의도: 시스템 내비게이션 바에 버튼이 가려지지 않게 navigationBarsPadding 적용
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
            ) { Text("저장") }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 계획 항목 연결: 위치 알림으로 들어왔으면 알림 문구(라벨 없이), 아니면 라벨 + 계획 항목 선택
            // 의도: 06(알림) 이미지는 라벨 없이 알림 박스만, 03(직접 선택) 이미지는 '계획 항목 연결' 라벨이 붙어 있어 그대로 따름
            val autoFillText = form.autoFillText
            if (autoFillText != null) {
                // 논의 필요: 알림 문구 칸을 눌러 계획 항목 선택으로 바꿀 수 있게 할지 정할 것
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null)
                        Text(autoFillText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                FormSection(label = "계획 항목 연결") {
                    Box {
                        OutlinedCard(
                            onClick = { planMenuOpen = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    planLink ?: "연결 안 함",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f),
                                )
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "계획 항목 선택")
                            }
                        }
                        DropdownMenu(
                            expanded = planMenuOpen,
                            onDismissRequest = { planMenuOpen = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("연결 안 함") },
                                onClick = {
                                    planLink = null
                                    planMenuOpen = false
                                },
                            )
                            state.planLinkOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        planLink = option
                                        planMenuOpen = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            // 항목명
            FormSection(label = "항목명") {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // 통화 + 금액 + 원화 환산
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box {
                    AssistChip(
                        onClick = { currencyMenuOpen = true },
                        label = { Text(currency) },
                        shape = CircleShape,
                        trailingIcon = {
                             Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "통화 선택",
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                    DropdownMenu(
                        expanded = currencyMenuOpen,
                        onDismissRequest = { currencyMenuOpen = false },
                    ) {
                        state.currencyOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    currency = option
                                    currencyMenuOpen = false
                                },
                            )
                        }
                    }
                }
                // 의도: 이미지처럼 화면 폭 전체가 아니라 입력 글자 폭 정도로만 좁게 둠
                TextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    textStyle = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.widthIn(max = 220.dp),
                )
                // 임시: 금액·통화를 바꿔도 환산 문구는 샘플 그대로. 원화 결제면 샘플에 문구 없음
                form.krwHintText?.let { krwHintText ->
                    Text(
                        krwHintText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // 결제 수단
            FormSection(label = "결제 수단") {
                SingleChoiceRow(
                    options = state.payMethodOptions,
                    selected = payMethod,
                    onSelect = { payMethod = it },
                )
            }

            // 카테고리 (접힘/펼침)
            FormSection(label = "카테고리") {
                // 의도: 접혀 있어도 선택된 카테고리는 항상 보이게 함
                val visibleCategories = if (categoriesExpanded) {
                    state.categoryOptions
                } else {
                    val head = state.categoryOptions.take(COLLAPSED_CATEGORY_COUNT)
                    if (category in head) head else head + category
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    visibleCategories.forEach { option ->
                        FilterChip(
                            selected = category == option,
                            onClick = { category = option },
                            label = { Text(option) },
                            shape = CircleShape,
                        )
                    }
                    if (state.categoryOptions.size > COLLAPSED_CATEGORY_COUNT) {
                        AssistChip(
                            onClick = { categoriesExpanded = !categoriesExpanded },
                            label = { Text(if (categoriesExpanded) "접기" else "…") },
                            shape = CircleShape,
                        )
                    }
                }
            }

            // 결제일 / 이용일
            // TODO: 누르면 DatePicker 띄울 것. 지금은 샘플 값만 보여줌
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormSection(label = "결제일", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = form.paidDateText,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                FormSection(label = "이용일", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = form.useDateText,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // 누가 결제했나요? (한 명만 선택)
            FormSection(label = "누가 결제했나요?") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.memberNames.forEach { name ->
                        FilterChip(
                            selected = payerName == name,
                            onClick = { payerName = name },
                            label = { Text(name) },
                            shape = CircleShape,
                        )
                    }
                }
            }

            // 누구와 나누나요? (여러 명 선택) + 1인 금액 + 분할 방식
            FormSection(label = "누구와 나누나요?") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        state.memberNames.forEach { name ->
                            val selected = name in splitMembers
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    splitMembers = if (selected) splitMembers - name else splitMembers + name
                                },
                                label = { Text(name) },
                                shape = CircleShape,
                                leadingIcon = if (selected) {
                                    {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                        }
                    }
                    // 임시: 나눌 사람을 바꿔도 1인 금액은 샘플 그대로. 1/N 계산은 기능 단계에서 할 것
                    if (isEvenSplit) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(form.perPersonText, style = MaterialTheme.typography.titleMedium)
                            form.perPersonKrwText?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
                SingleChoiceRow(
                    options = state.splitModeOptions,
                    selected = splitMode,
                    onSelect = { splitMode = it },
                )
                // 의도: 직접 입력이면 나눌 사람마다 금액 칸을 보여줌. 값은 빈 칸에서 시작
                if (!isEvenSplit) {
                    state.memberNames.filter { it in splitMembers }.forEach { name ->
                        OutlinedTextField(
                            value = directAmounts[name].orEmpty(),
                            onValueChange = { directAmounts[name] = it },
                            label = { Text(name) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            // 정산 확인 안 함
            ListItem(
                headlineContent = { Text("정산 확인 안 함") },
                supportingContent = { Text("켜면 정산 목록에서 빠져요") },
                trailingContent = {
                    Switch(
                        checked = settlementExcluded,
                        onCheckedChange = { settlementExcluded = it },
                    )
                },
            )

            // 메모 (선택 입력)
            FormSection(label = "메모") {
                OutlinedTextField(
                    value = memoInput,
                    onValueChange = { memoInput = it },
                    placeholder = { Text("선택 입력") },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

// 의도: 섹션 라벨 + 내용 묶음. 이 화면 안에서만 씀
@Composable
private fun FormSection(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        content()
    }
}

// 의도: 결제 수단·분할 방식처럼 둘 중 하나 고르는 세그먼트 버튼
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleChoiceRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selected == option,
                onClick = { onSelect(option) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
            ) { Text(option) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseRecordFormScreenPreview() {
    ExpenseRecordFormScreen(
        state = ExpenseRecordFormUiState(),
        onBack = {},
        onCameraClick = {},
        onSaveClick = {},
    )
}
