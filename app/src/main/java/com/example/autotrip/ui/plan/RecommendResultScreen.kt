package com.example.autotrip.ui.plan

// Epic: 여행 계획 생성

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.common.BackTopBar
import java.time.LocalDate

private data class ScheduleItem(
    val time: String,
    val title: String
)

private data class DayPlan(
    val date: LocalDate,
    val items: List<ScheduleItem>
)

// TODO: PlanRepository 연결 전까지 사용하는 샘플 일정
private fun sampleDayPlans(): List<DayPlan> {
    val start = LocalDate.now()

    return listOf(
        DayPlan(
            date = start,
            items = listOf(
                ScheduleItem("9:00", "여행지 추천"),
                ScheduleItem("10:00", "여행지 추천"),
                ScheduleItem("12:00", "식당 추천"),
                ScheduleItem("14:00", "관광지 추천"),
                ScheduleItem("18:00", "식당 추천")
            )
        ),
        DayPlan(
            date = start.plusDays(1),
            items = listOf(
                ScheduleItem("9:30", "관광지 추천"),
                ScheduleItem("12:00", "식당 추천"),
                ScheduleItem("15:00", "여행지 추천"),
                ScheduleItem("19:00", "식당 추천")
            )
        ),
        DayPlan(
            date = start.plusDays(2),
            items = listOf(
                ScheduleItem("10:00", "여행지 추천"),
                ScheduleItem("12:30", "식당 추천"),
                ScheduleItem("15:00", "관광지 추천")
            )
        )
    )
}

// TODO: 실제 추천 API 연결 전까지 "장소 바꾸기"에 사용하는 샘플 후보
private val alternativePlaces = listOf(
    "카페 추천",
    "쇼핑 추천",
    "공원 추천",
    "전시 추천",
    "야경 명소 추천"
)

private fun nextAlternative(current: String): String {
    val index = alternativePlaces.indexOf(current)
    return alternativePlaces[(index + 1) % alternativePlaces.size]
}

@Composable
fun RecommendResultScreen(
    onBackClick: () -> Unit = {},
    onRegenerateClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    var dayPlans by remember { mutableStateOf(sampleDayPlans()) }
    var dayIndex by remember { mutableStateOf(0) }
    var isEdited by remember { mutableStateOf(false) }
    var showRegenerateDialog by remember { mutableStateOf(false) }

    fun updateItems(
        targetDay: Int,
        transform: (List<ScheduleItem>) -> List<ScheduleItem>
    ) {
        isEdited = true
        dayPlans = dayPlans.mapIndexed { index, plan ->
            if (index == targetDay) {
                plan.copy(items = transform(plan.items))
            } else {
                plan
            }
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "추천 일정",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {

            DateNavigator(
                date = dayPlans[dayIndex].date,
                canGoPrevious = dayIndex > 0,
                canGoNext = dayIndex < dayPlans.lastIndex,
                onPreviousClick = { dayIndex-- },
                onNextClick = { dayIndex++ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedContent(
                targetState = dayIndex,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "dayPlan"
            ) { index ->
                DayTimelineCard(
                    dayNumber = index + 1,
                    items = dayPlans[index].items,
                    onReplaceClick = { itemIndex ->
                        updateItems(index) { items ->
                            items.mapIndexed { i, item ->
                                if (i == itemIndex) {
                                    item.copy(title = nextAlternative(item.title))
                                } else {
                                    item
                                }
                            }
                        }
                    },
                    onDeleteClick = { itemIndex ->
                        updateItems(index) { items ->
                            items.filterIndexed { i, _ -> i != itemIndex }
                        }
                    }
                )
            }

            OutlinedButton(
                onClick = {
                    if (isEdited) {
                        showRegenerateDialog = true
                    } else {
                        onRegenerateClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "다시 생성하기",
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .height(56.dp)
            ) {
                Text(
                    text = "일정 확정하기",
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showRegenerateDialog) {
        AlertDialog(
            onDismissRequest = { showRegenerateDialog = false },
            title = { Text(text = "일정을 다시 생성할까요?") },
            text = { Text(text = "수정한 내용이 사라져요.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegenerateDialog = false
                        onRegenerateClick()
                    }
                ) {
                    Text(text = "다시 생성")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRegenerateDialog = false }
                ) {
                    Text(text = "취소")
                }
            }
        )
    }
}

@Composable
private fun DateNavigator(
    date: LocalDate,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ArrowButton(
            text = "‹",
            enabled = canGoPrevious,
            onClick = onPreviousClick
        )

        Text(
            text = date.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        ArrowButton(
            text = "›",
            enabled = canGoNext,
            onClick = onNextClick
        )
    }
}

@Composable
private fun ArrowButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    }
}

@Composable
private fun DayTimelineCard(
    dayNumber: Int,
    items: List<ScheduleItem>,
    onReplaceClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {

        Text(
            text = "DAY $dayNumber",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "일정이 없어요",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Column
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            items.forEachIndexed { index, item ->
                key(item) {
                    TimelineRow(
                        item = item,
                        onReplaceClick = { onReplaceClick(index) },
                        onDeleteClick = { onDeleteClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(
    item: ScheduleItem,
    onReplaceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        Text(
            text = item.time,
            modifier = Modifier
                .width(52.dp)
                .padding(top = 24.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outline)
        )

        Spacer(modifier = Modifier.width(12.dp))

        ScheduleCard(
            title = item.title,
            onReplaceClick = onReplaceClick,
            onDeleteClick = onDeleteClick,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun ScheduleCard(
    title: String,
    onReplaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(8.dp)
            )
    ) {

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 14.sp
        )

        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = "⋮",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showMenu = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "장소 바꾸기") },
                    onClick = {
                        showMenu = false
                        onReplaceClick()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "삭제",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    }
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
private fun RecommendResultScreenPreview() {
    RecommendResultScreen()
}
