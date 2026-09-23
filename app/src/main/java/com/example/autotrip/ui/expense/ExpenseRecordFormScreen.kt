package com.example.autotrip.ui.expense

// 06 지출 기록

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 임시: 뼈대 단계라 상단바와 저장 버튼만 둠. U5에서 와이어프레임 배치로 교체할 것
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseRecordFormScreen(
    state: ExpenseRecordFormUiState,
    onBack: () -> Unit,
    onCameraClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditMode) "지출 수정" else "지출 기록") },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 임시: 수정 모드 진입 확인용으로 샘플 항목 이름만 보여줌
            state.editingExpense?.let { Text(it.title) }
            Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth()) { Text("저장") }
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
