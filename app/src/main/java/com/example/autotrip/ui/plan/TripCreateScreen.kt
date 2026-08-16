package com.example.autotrip.ui.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TripCreateScreen() {

    var destination by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {

        // 여행지
        if (destination.isEmpty()) {

            Text(
                text = "어디로 가시나요?",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = destination,
                onValueChange = {
                    destination = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("여행지를 입력해주세요")
                },
                singleLine = true
            )

        } else {

            // 입력 완료 후 여행지만 표시
            Text(
                text = destination,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 일정
        Text(
            text = "일정을 입력해주세요",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "달력은 다음 단계에서 추가",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 예산
        Text(
            text = "예산을 입력해주세요",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = budget,
            onValueChange = {
                budget = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("00만원")
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 여행 스타일
        Text(
            text = "원하는 여행 스타일을 선택해주세요",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "관광 · 쇼핑 · 음식 · 액티비티",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 일정 생성
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "일정 생성",
                fontSize = 18.sp
            )
        }
    }
}