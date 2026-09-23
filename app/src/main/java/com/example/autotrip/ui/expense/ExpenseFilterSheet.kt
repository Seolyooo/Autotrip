package com.example.autotrip.ui.expense

// 10 정렬·필터

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 의도: 09 위에 뜨는 바텀시트. 시트 밖 터치·시스템 뒤로·적용 모두 onBack으로 닫힘
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
// 임시: 뼈대 단계라 적용 버튼만 둠. U7에서 와이어프레임 배치로 교체할 것
@Composable
private fun ExpenseFilterSheetContent(
    state: ExpenseFilterUiState,
    onApplyClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("정렬·필터")
        Text(state.memberNames.joinToString(" · "))
        Button(onClick = onApplyClick, modifier = Modifier.fillMaxWidth()) { Text("적용") }
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
