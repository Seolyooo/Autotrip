package com.example.autotrip.ui.plan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.launch

/*
 * ---------------------------------------------------------
 * 커스텀 달력 (Kizitonwose Calendar)
 * ---------------------------------------------------------
 */

@Composable
internal fun TripCalendar(
    initialMonth: YearMonth,
    selectedStart: LocalDate?,
    selectedEnd: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {

    val daysOfWeek = remember {
        daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
    }
    val startMonth = remember { YearMonth.now().minusYears(5) }
    val endMonth = remember { YearMonth.now().plusYears(10) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = initialMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val visibleMonth = state.firstVisibleMonth.yearMonth
    val coroutineScope = rememberCoroutineScope()
    var showYearMenu by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val rangeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Column(modifier = Modifier.fillMaxWidth()) {

        Box(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            coroutineScope.launch {
                                state.animateScrollToMonth(visibleMonth.minusMonths(1))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "‹",
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${visibleMonth.year}년 ${visibleMonth.monthValue}월",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showYearMenu = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            coroutineScope.launch {
                                state.animateScrollToMonth(visibleMonth.plusMonths(1))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "›",
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            DropdownMenu(
                expanded = showYearMenu,
                onDismissRequest = { showYearMenu = false },
                modifier = Modifier
                    .height(300.dp)
                    .width(180.dp)
            ) {
                val currentYear = LocalDate.now().year
                val years = (currentYear - 5)..(currentYear + 10)

                years.forEach { year ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${year}년",
                                fontWeight = if (year == visibleMonth.year) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                state.scrollToMonth(
                                    YearMonth.of(year, visibleMonth.monthValue)
                                )
                            }
                            showYearMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CalendarWeekHeader(daysOfWeek = daysOfWeek)

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalCalendar(
            state = state,
            userScrollEnabled = false,
            dayContent = { day ->
                TripCalendarDay(
                    day = day,
                    selectedStart = selectedStart,
                    selectedEnd = selectedEnd,
                    rangeColor = rangeColor,
                    primaryColor = primaryColor,
                    onClick = onDateClick
                )
            }
        )
    }
}

@Composable
private fun CalendarWeekHeader(daysOfWeek: List<DayOfWeek>) {

    val labels = mapOf(
        DayOfWeek.SUNDAY to "일",
        DayOfWeek.MONDAY to "월",
        DayOfWeek.TUESDAY to "화",
        DayOfWeek.WEDNESDAY to "수",
        DayOfWeek.THURSDAY to "목",
        DayOfWeek.FRIDAY to "금",
        DayOfWeek.SATURDAY to "토"
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labels.getValue(day),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TripCalendarDay(
    day: CalendarDay,
    selectedStart: LocalDate?,
    selectedEnd: LocalDate?,
    rangeColor: Color,
    primaryColor: Color,
    onClick: (LocalDate) -> Unit
) {

    val inMonth = day.position == DayPosition.MonthDate
    val date = day.date
    val isStart = inMonth && date == selectedStart
    val isEnd = inMonth && date == selectedEnd
    val isInRange = inMonth &&
        selectedStart != null &&
        selectedEnd != null &&
        date.isAfter(selectedStart) &&
        date.isBefore(selectedEnd)
    val isSelected = isStart || isEnd

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(enabled = inMonth) { onClick(date) },
        contentAlignment = Alignment.Center
    ) {

        if (!inMonth) return@Box

        if (isInRange) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(rangeColor)
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = primaryColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 15.sp,
                color = if (isInRange) {
                    primaryColor
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}
