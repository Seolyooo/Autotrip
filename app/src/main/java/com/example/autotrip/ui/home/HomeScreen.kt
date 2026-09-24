package com.example.autotrip.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.home.components.BudgetPreviewWidget
import com.example.autotrip.ui.home.components.ItineraryCard
import com.example.autotrip.ui.home.components.SegmentedToggle

/**
 * 메인 홈.
 *
 * 의도:
 * - "가계부 화면으로" 버튼을 따로 두지 않고, 예산 미리보기 영역 자체를 눌러서 이동하게 함.
 * - 일정 카드 / 지도 / 예산 미리보기 세 영역이 남은 화면을 정확히 1/3씩 나눠 갖도록 weight 씀.
 * - 여행/로그 토글은 원래 버튼이 있던 자리에 고정 배치함 (1/3 분할 영역 밖, 하단 고정).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTravelClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    onLogClick: (String) -> Unit = {},
    selectedTabIndex: Int = 0,
    onTabChange: (Int) -> Unit = {}
) {
    val isLogTab = selectedTabIndex == 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auto Trip") },
                actions = {
                    if (!isLogTab) {
                        IconButton(onClick = onAddClick) {
                            Icon(Icons.Default.Add, contentDescription = "추가")
                        }
                    }
                    IconButton(onClick = { /* TODO: 알림 */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "알림")
                    }
                    IconButton(onClick = { /* TODO: 더보기 메뉴 */ }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "더보기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            if (isLogTab) {
                LogScreen(
                    onLogClick = onLogClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                TravelContent(
                    onTravelClick = onTravelClick,
                    onNavigateToBudget = onNavigateToBudget,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 의도: 여행/로그 토글은 아래에 고정 배치함
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SegmentedToggle(
                    options = listOf("여행", "로그"),
                    selectedIndex = selectedTabIndex,
                    onSelectedChange = onTabChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 홈의 "여행" 탭 내용. 일정 카드 / 지도 / 예산 미리보기 세 영역이 1/3씩 나눠 가짐.
 */
@Composable
private fun TravelContent(
    onTravelClick: () -> Unit,
    onNavigateToBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 임시: 목업 데이터 넣어둠. 실제 여행 일정 데이터는 API 붙일 때 교체할 것
        ItineraryCard(
            dayLabel = "DAY 1",
            currentTime = "12:00",
            currentPlace = "점심",
            nextTime = "14:00",
            nextPlace = "쇼핑센터",
            progress = 0.45f,
            onSeeAllClick = onTravelClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 임시: 실제 Google Maps 연동 전까지 자리만 잡아둠
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "지도 영역 (연동 예정)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 의도: 별도 버튼 없이 이 영역 자체를 누르면 가계부 화면으로 이동하게 함
        BudgetPreviewWidget(
            totalBudgetLabel = "500,000원",
            spentSoFarLabel = "213,000원",
            remainingLabel = "287,000원",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { onNavigateToBudget() }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
