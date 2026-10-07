package com.example.autotrip.ui.diary

// Epic: 여행기록

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.ui.diary.components.MemberPhotoCard
import kotlinx.coroutines.launch

private data class LogMember(
    val name: String,
    val isMe: Boolean = false
)

// 임시: 목업 데이터 넣어둠. 실제 동행인/여행 일정은 API 붙일 때 교체할 것
private val sampleMembers = listOf(
    LogMember("나", isMe = true),
    LogMember("설식이"),
    LogMember("밍가뎀")
)

private enum class ScheduleTime(val label: String) {
    Past("지난 일정"),
    Current("지금"),
    Future("다음 일정")
}

private data class LogPlace(
    val name: String,
    val time: ScheduleTime
)

private val samplePlaces = listOf(
    LogPlace("도쿄타워", ScheduleTime.Past),
    LogPlace("이치란 라멘", ScheduleTime.Current),
    LogPlace("시부야 쇼핑", ScheduleTime.Future)
)

/**
 * 로그 카드를 눌렀을 때 나오는 여행 기록 화면.
 * 여행 일정(장소) 하나가 한 페이지. 처음엔 현재 일정이 보이고, 왼쪽은 과거 / 오른쪽은 미래.
 * 좌우로 밀거나 화면 양쪽 가장자리를 눌러서 이동함. 내 카드를 누르면 카메라가 열림.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripLogDetailScreen(
    tripTitle: String,
    onBackClick: () -> Unit = {}
) {
    val myPhotos = remember { mutableStateMapOf<Int, Uri>() }
    var cameraPage by remember { mutableStateOf<Int?>(null) }
    val pagerState = rememberPagerState(
        initialPage = samplePlaces
            .indexOfFirst { it.time == ScheduleTime.Current }
            .coerceAtLeast(0),
        pageCount = { samplePlaces.size }
    )
    val scope = rememberCoroutineScope()

    val capturingPage = cameraPage

    if (capturingPage != null) {

        BackHandler { cameraPage = null }

        CameraCaptureScreen(
            onCaptured = { uri ->
                myPhotos[capturingPage] = uri
                cameraPage = null
            },
            onClose = { cameraPage = null }
        )

    } else {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(tripTitle) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: 공유 */ }) {
                            Icon(Icons.Default.IosShare, contentDescription = "공유")
                        }
                        IconButton(onClick = { /* TODO: 채팅 */ }) {
                            BadgedBox(
                                badge = { Badge { Text("2") } }
                            ) {
                                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "채팅")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

                PageIndicator(
                    pageCount = samplePlaces.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Text(
                    text = samplePlaces[pagerState.currentPage].time.label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            sampleMembers.forEach { member ->
                                MemberPhotoCard(
                                    memberName = member.name,
                                    placeName = samplePlaces[page].name,
                                    photoUri = if (member.isMe) myPhotos[page] else null,
                                    isMe = member.isMe,
                                    onClick = if (member.isMe) {
                                        { cameraPage = page }
                                    } else {
                                        null
                                    }
                                )
                            }
                        }
                    }

                    EdgeTapZone(
                        enabled = pagerState.currentPage > 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    )

                    EdgeTapZone(
                        enabled = pagerState.currentPage < samplePlaces.lastIndex,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}

@Composable
private fun EdgeTapZone(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(40.dp)
            .fillMaxHeight()
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage

            Box(
                modifier = Modifier
                    .size(if (selected) 8.dp else 6.dp)
                    .background(
                        color = if (selected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun TripLogDetailScreenPreview() {
    TripLogDetailScreen(tripTitle = "도쿄 여행")
}
