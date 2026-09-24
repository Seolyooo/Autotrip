package com.example.autotrip.ui.plan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.plan.tripStyles
import java.time.YearMonth

@Composable
internal fun DestinationStep(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        StepTitle(text = "어디로 가시나요?")

        OutlineInputCard(
            value = value,
            onValueChange = onValueChange,
            placeholder = "예: 도쿄, 부산",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onConfirm()
                }
            )
        )

        NextButton(
            text = "다음",
            enabled = value.isNotBlank(),
            onClick = onConfirm
        )
    }
}

/*
 * ---------------------------------------------------------
 * 날짜 선택
 * ---------------------------------------------------------
 */

@Composable
internal fun DatesStep(
    initialStart: Long?,
    initialEnd: Long?,
    onConfirm: (Long, Long) -> Unit
) {

    val initialStartDate =
        initialStart?.let { millisToLocalDate(it) }

    val initialEndDate =
        initialEnd?.let { millisToLocalDate(it) }

    var selectedStart by remember(initialStart) {
        mutableStateOf(initialStartDate)
    }

    var selectedEnd by remember(initialEnd) {
        mutableStateOf(initialEndDate)
    }

    val canConfirm =
        selectedStart != null &&
            selectedEnd != null

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        StepTitle(text = "언제 여행가세요?")

        /*
         * 선택된 날짜 표시
         */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DateSummaryCard(
                title = "여행 시작",
                dateMillis = selectedStart?.let {
                    localDateToMillis(it)
                },
                modifier = Modifier.weight(1f)
            )

            DateSummaryCard(
                title = "여행 종료",
                dateMillis = selectedEnd?.let {
                    localDateToMillis(it)
                },
                modifier = Modifier.weight(1f)
            )
        }

        /*
         * 달력
         */
        TripCalendar(
            initialMonth = initialStartDate?.let {
                YearMonth.from(it)
            } ?: YearMonth.now(),
            selectedStart = selectedStart,
            selectedEnd = selectedEnd,
            onDateClick = { date ->

                /*
                 * 아직 시작일이 없거나
                 * 시작/종료가 모두 선택된 상태라면
                 * 새로운 시작일을 선택
                 */
                if (
                    selectedStart == null ||
                    selectedEnd != null
                ) {

                    selectedStart = date
                    selectedEnd = null

                } else {

                    /*
                     * 시작일 이후를 누르면 종료일
                     */
                    if (!date.isBefore(selectedStart)) {

                        selectedEnd = date

                    } else {

                        /*
                         * 시작일보다 이전을 누르면
                         * 해당 날짜를 새로운 시작일로 설정
                         */
                        selectedStart = date
                        selectedEnd = null
                    }
                }
            }
        )

        NextButton(
            text = "선택완료",
            enabled = canConfirm,
            onClick = {

                val start =
                    selectedStart ?: return@NextButton

                val end =
                    selectedEnd ?: return@NextButton

                onConfirm(
                    localDateToMillis(start),
                    localDateToMillis(end)
                )
            }
        )
    }
}

/*
 * ---------------------------------------------------------
 * 날짜 표시 카드
 * ---------------------------------------------------------
 */

@Composable
private fun DateSummaryCard(
    title: String,
    dateMillis: Long?,
    modifier: Modifier = Modifier
) {

    val dateText =
        if (dateMillis != null) {

            val date =
                millisToLocalDate(dateMillis)

            "${date.monthValue}월 ${date.dayOfMonth}일"

        } else {
            "날짜 선택"
        }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {

        Text(
            text = title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = dateText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/*
 * ---------------------------------------------------------
 * 예산
 * ---------------------------------------------------------
 */

@Composable
internal fun BudgetStep(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        StepTitle(text = "예산은 얼마인가요?")

        OutlineInputCard(
            value = value,
            onValueChange = onValueChange,
            placeholder = "금액 입력 (만원)",
            suffix =
                if (value.isEmpty()) {
                    ""
                } else {
                    "만원"
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onConfirm()
                }
            )
        )

        NextButton(
            text = "다음",
            enabled = value.isNotBlank(),
            onClick = onConfirm
        )
    }
}

/*
 * ---------------------------------------------------------
 * 여행 스타일
 * ---------------------------------------------------------
 */

@Composable
internal fun StyleStep(
    selectedStyles: Set<String>,
    onToggle: (String) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        StepTitle(text = "어떤 여행을 원하세요?")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            tripStyles.forEach { style ->

                val selected =
                    style in selectedStyles

                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                        .background(
                            if (selected) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onToggle(style)
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        )
                ) {

                    Text(
                        text = style,
                        color =
                            if (selected) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        fontWeight =
                            if (selected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                    )
                }
            }
        }
    }
}
