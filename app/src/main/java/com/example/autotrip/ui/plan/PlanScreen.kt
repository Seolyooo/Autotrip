package com.example.autotrip.ui.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanScreen(
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            // 뒤로가기 버튼
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Text(
                        text = "←",
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 제목
            Text(
                text = "여행을 떠나시나요?",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 설명
            Text(
                text = "여행지, 일정, 예산만 입력하면\nAI가 맞춤 여행 일정을 만들어드립니다.",
                fontSize = 16.sp
            )
        }

        // 시작하기 버튼
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "시작하기",
                fontSize = 18.sp
            )
        }
    }
}