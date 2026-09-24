package com.example.autotrip.ui.expense

// 10 정렬·필터

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 의도: 09 위에 뜨는 바텀시트. 시트 밖 터치·시스템 뒤로·X·'N건 보기' 모두 onBack으로 닫힘
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFilterSheet(
    state: ExpenseFilterUiState,
    onBack: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onBack) {
        ExpenseFilterSheetContent(state = state, onApplyClick = onBack)
    }
}

// 의도: ModalBottomSheet는 Preview에 안 그려져서 내용만 따로 분리함
// 임시: 선택을 바꿔도 'N건 보기' 숫자는 샘플 값 그대로 (계산은 기능 단계). '초기화'는 진입 시 값으로 되돌림
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ExpenseFilterSheetContent(
    state: ExpenseFilterUiState,
    onApplyClick: () -> Unit,
) {
    var selectedSort by remember(state) { mutableStateOf(state.selectedSort) }
    var dateFrom by remember(state) { mutableStateOf(state.dateFromText) }
    var dateTo by remember(state) { mutableStateOf(state.dateToText) }
    var selectedDateBasis by remember(state) { mutableStateOf(state.selectedDateBasis) }
    var selectedTrip by remember(state) { mutableStateOf(state.selectedTrip) }
    var selectedSplit by remember(state) { mutableStateOf(state.selectedSplit) }
    var selectedPayer by remember(state) { mutableStateOf(state.selectedPayer) }
    var selectedPayMethod by remember(state) { mutableStateOf(state.selectedPayMethod) }
    var selectedSettlement by remember(state) { mutableStateOf(state.selectedSettlement) }

    fun resetAll() {
        selectedSort = state.selectedSort
        dateFrom = state.dateFromText
        dateTo = state.dateToText
        selectedDateBasis = state.selectedDateBasis
        selectedTrip = state.selectedTrip
        selectedSplit = state.selectedSplit
        selectedPayer = state.selectedPayer
        selectedPayMethod = state.selectedPayMethod
        selectedSettlement = state.selectedSettlement
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("정렬·필터", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onApplyClick) {
                Icon(Icons.Filled.Close, contentDescription = "닫기")
            }
        }

        FilterSection(label = "정렬") {
            SingleChoiceChipRow(
                options = state.sortOptions,
                selected = selectedSort,
                onSelect = { selectedSort = it },
            )
        }

        FilterSection(label = "날짜") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // TODO: 누르면 DatePicker 띄울 것. 지금은 샘플 값만 보여줌
                OutlinedTextField(
                    value = dateFrom,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Text("~")
                OutlinedTextField(
                    value = dateTo,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            SingleChoiceChipRow(
                options = state.dateBasisOptions,
                selected = selectedDateBasis,
                onSelect = { selectedDateBasis = it },
            )
        }

        FilterSection(label = "여행") {
            SingleChoiceChipRow(
                options = state.tripOptions,
                selected = selectedTrip,
                onSelect = { selectedTrip = it },
            )
        }

        FilterSection(label = "나누기") {
            SingleChoiceChipRow(
                options = state.splitOptions,
                selected = selectedSplit,
                onSelect = { selectedSplit = it },
            )
        }

        FilterSection(label = "결제한 사람") {
            SingleChoiceChipRow(
                options = state.payerOptions,
                selected = selectedPayer,
                onSelect = { selectedPayer = it },
            )
        }

        FilterSection(label = "결제 수단") {
            SingleChoiceChipRow(
                options = state.payMethodOptions,
                selected = selectedPayMethod,
                onSelect = { selectedPayMethod = it },
            )
        }

        FilterSection(label = "정산 상태") {
            SingleChoiceChipRow(
                options = state.settlementOptions,
                selected = selectedSettlement,
                onSelect = { selectedSettlement = it },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(onClick = ::resetAll, modifier = Modifier.weight(1f)) {
                Text("초기화")
            }
            Button(onClick = onApplyClick, modifier = Modifier.weight(1.5f)) {
                Text(state.resultCountButtonText)
            }
        }
    }
}

// 의도: 섹션 라벨 + 내용 묶음. 이 화면 안에서만 씀 (06의 FormSection과 같은 역할)
@Composable
private fun FilterSection(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        content()
    }
}

// 의도: 정렬·나누기·결제 수단처럼 하나만 고르는 칩 목록
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SingleChoiceChipRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseFilterSheetPreview() {
    ExpenseFilterSheetContent(
        state = ExpenseFilterUiState(),
        onApplyClick = {},
    )
}
