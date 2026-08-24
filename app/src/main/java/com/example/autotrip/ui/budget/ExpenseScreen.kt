package com.example.autotrip.ui.budget
// Epic: 예산관리

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.theme.CategoryEtc
import com.example.autotrip.ui.theme.CategoryFood
import com.example.autotrip.ui.theme.CategoryLodging
import com.example.autotrip.ui.theme.CategoryShopping
import com.example.autotrip.ui.theme.CategoryTransport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 임시: 카테고리명 - 가계부 차트 색상 매핑. 카테고리 늘어나면 여기도 같이 추가할 것
private val categoryColors = mapOf(
    "식비" to CategoryFood,
    "쇼핑" to CategoryShopping,
    "교통" to CategoryTransport,
    "숙박" to CategoryLodging,
    "기타" to CategoryEtc
)

// 임시: 실제 일정 데이터 연동 전까지 쓰는 목업 일정 목록. UI 텍스트 아님, 코드 참고용 메모.
private val mockSchedules = listOf("연결 안 함", "1일차 · 점심", "1일차 · 쇼핑센터", "2일차 · 박물관")

/**
 * 지출 직접 입력 화면. "직접 입력" 버튼으로 진입.
 * 의도: 장소 필드는 따로 안 두고, 연결된 일정 선택으로 위치 정보를 대신함.
 *
 * 논의 필요: 여러 명이 나눠 낸 지출(숙소비 등) 정산(1/N) 기능은 아이디어로만 남겨두고
 * 지금은 구현 안 함. 동행자 계정·정산 데이터 구조가 아직 없어서 지금 넣기엔 범위가 커짐.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (ExpenseItem) -> Unit = {}
) {
    val categories = categoryColors.keys.toList()
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var amountText by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    var selectedSchedule by remember { mutableStateOf(mockSchedules.first()) }
    var paymentMethod by remember { mutableStateOf("현금") }

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var scheduleMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("지출 추가") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                // 의도: 내용이 화면보다 길어지면 세로로 스크롤되게 함
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            FieldLabel("금액", required = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { input -> amountText = input.filter { it.isDigit() } },
                placeholder = { Text("0") },
                suffix = { Text("원") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            FieldLabel("카테고리", required = true)
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val color = categoryColors[category] ?: MaterialTheme.colorScheme.primary
                    val selected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(color.copy(alpha = if (selected) 1f else 0.18f))
                            .border(
                                width = if (selected) 2.dp else 1.dp,
                                color = color,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (selected) Color.White else color,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel("날짜", required = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formatDate(selectedDateMillis),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "날짜 선택")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { selectedDateMillis = it }
                            showDatePicker = false
                        }) { Text("확인") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("취소") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel("연결된 일정")
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = scheduleMenuExpanded,
                onExpandedChange = { scheduleMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedSchedule,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = scheduleMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = scheduleMenuExpanded,
                    onDismissRequest = { scheduleMenuExpanded = false }
                ) {
                    mockSchedules.forEach { schedule ->
                        DropdownMenuItem(
                            text = { Text(schedule) },
                            onClick = {
                                selectedSchedule = schedule
                                scheduleMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            FieldLabel("메모")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                placeholder = { Text("메모를 입력하세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Spacer(Modifier.height(24.dp))

            FieldLabel("결제 수단", required = true)
            Spacer(Modifier.height(8.dp))
            PaymentMethodToggle(
                options = listOf("현금", "카드"),
                selected = paymentMethod,
                onSelectedChange = { paymentMethod = it }
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    val place = if (selectedSchedule == mockSchedules.first()) "" else selectedSchedule
                    onSaveClick(
                        ExpenseItem(
                            category = selectedCategory,
                            place = place,
                            amount = amount,
                            date = formatDate(selectedDateMillis)
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("저장")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// 의도: 라벨 옆에 필수 항목이면 빨간 * 표시. 없으면 선택 항목이라는 뜻.
@Composable
private fun FieldLabel(text: String, required: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (required) {
            Spacer(Modifier.width(2.dp))
            Text(
                "*",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

// 결제수단처럼 "둘 중 하나, 전체 너비 반반"인 토글용. 여행/로그 토글(SegmentedToggle)은
// 내용 크기만큼만 작게 잡히는 스타일이라 이건 따로 만듦.
@Composable
private fun PaymentMethodToggle(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .padding(4.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                    .clickable { onSelectedChange(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ExpenseEntryScreenPreview() {
    ExpenseEntryScreen()
}