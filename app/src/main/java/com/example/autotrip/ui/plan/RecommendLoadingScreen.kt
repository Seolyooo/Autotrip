package com.example.autotrip.ui.plan

// Epic: 여행 계획 생성

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val loadingMessages = listOf(
    "여행지 정보를 확인하고 있어요",
    "동선을 계산하고 있어요",
    "예산에 맞게 조정하고 있어요"
)

private const val MESSAGE_DURATION_MILLIS = 1500L

@Composable
fun RecommendLoadingScreen(
    onFinished: () -> Unit = {}
) {
    var messageIndex by remember { mutableStateOf(0) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    // TODO: PlanRepository 연결 전까지는 고정 시간만큼 기다린 뒤 완료 처리
    LaunchedEffect(Unit) {
        loadingMessages.indices.forEach { index ->
            messageIndex = index
            delay(MESSAGE_DURATION_MILLIS)
        }
        currentOnFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "여행 일정을 만들고 있어요",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedContent(
                targetState = messageIndex,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "loadingMessage"
            ) { index ->
                Text(
                    text = loadingMessages[index],
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun RecommendLoadingScreenPreview() {
    RecommendLoadingScreen()
}
