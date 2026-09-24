package com.example.autotrip.ui.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.common.BackTopBar

@Composable
fun PlanScreen(
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Spacer(modifier = Modifier.height(48.dp))

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
}
