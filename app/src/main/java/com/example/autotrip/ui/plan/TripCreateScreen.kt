package com.example.autotrip.ui.plan

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.common.BackTopBar
import com.example.autotrip.ui.plan.components.BudgetStep
import com.example.autotrip.ui.plan.components.CollapsibleSummary
import com.example.autotrip.ui.plan.components.DatesStep
import com.example.autotrip.ui.plan.components.DestinationStep
import com.example.autotrip.ui.plan.components.StyleStep
import com.example.autotrip.ui.plan.components.formatKoreanDateRange

private enum class TripCreateStep {
    Destination,
    Dates,
    Budget,
    Style
}

internal val tripStyles = listOf(
    "관광",
    "쇼핑",
    "음식",
    "액티비티"
)

@Composable
fun TripCreateScreen(
    onBackClick: () -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    var step by remember { mutableStateOf(TripCreateStep.Destination) }

    var destination by remember { mutableStateOf("") }
    var startDateMillis by remember { mutableStateOf<Long?>(null) }
    var endDateMillis by remember { mutableStateOf<Long?>(null) }
    var budget by remember { mutableStateOf("") }
    var selectedStyles by remember { mutableStateOf(setOf<String>()) }

    val canCreate = destination.isNotBlank() &&
        startDateMillis != null &&
        endDateMillis != null &&
        budget.isNotBlank() &&
        selectedStyles.isNotEmpty()

    fun goBack() {
        step = when (step) {
            TripCreateStep.Destination -> {
                onBackClick()
                return
            }

            TripCreateStep.Dates ->
                TripCreateStep.Destination

            TripCreateStep.Budget ->
                TripCreateStep.Dates

            TripCreateStep.Style ->
                TripCreateStep.Budget
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "여행 만들기",
                onBackClick = { goBack() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                AccordionBody(
                    step = step,
                    destination = destination,
                    startDateMillis = startDateMillis,
                    endDateMillis = endDateMillis,
                    budget = budget,
                    selectedStyles = selectedStyles,

                    onDestinationChange = {
                        destination = it
                    },

                    onDestinationConfirm = {
                        if (destination.isBlank()) return@AccordionBody
                        step = TripCreateStep.Dates
                    },

                    onDatesConfirm = { start, end ->
                        startDateMillis = start
                        endDateMillis = end
                        step = TripCreateStep.Budget
                    },

                    onBudgetChange = {
                        budget = it.filter(Char::isDigit)
                    },

                    onBudgetConfirm = {
                        if (budget.isBlank()) return@AccordionBody
                        step = TripCreateStep.Style
                    },

                    onStyleToggle = { style ->
                        selectedStyles =
                            if (style in selectedStyles) {
                                selectedStyles - style
                            } else {
                                selectedStyles + style
                            }
                    },

                    onEditStep = {
                        step = it
                    }
                )
            }

            AnimatedVisibility(
                visible = canCreate,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {

                Button(
                    onClick = onCreateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 12.dp,
                            bottom = 8.dp
                        )
                        .height(56.dp)
                ) {
                    Text(
                        text = "일정 생성하기",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AccordionBody(
    step: TripCreateStep,
    destination: String,
    startDateMillis: Long?,
    endDateMillis: Long?,
    budget: String,
    selectedStyles: Set<String>,
    onDestinationChange: (String) -> Unit,
    onDestinationConfirm: () -> Unit,
    onDatesConfirm: (Long, Long) -> Unit,
    onBudgetChange: (String) -> Unit,
    onBudgetConfirm: () -> Unit,
    onStyleToggle: (String) -> Unit,
    onEditStep: (TripCreateStep) -> Unit
) {

    val showDestinationSummary =
        destination.isNotBlank() &&
            step != TripCreateStep.Destination

    val showDatesSummary =
        destination.isNotBlank() &&
            startDateMillis != null &&
            endDateMillis != null &&
            step != TripCreateStep.Dates

    val showBudgetSummary =
        destination.isNotBlank() &&
            startDateMillis != null &&
            endDateMillis != null &&
            budget.isNotBlank() &&
            step != TripCreateStep.Budget

    val showStyleSummary =
        destination.isNotBlank() &&
            startDateMillis != null &&
            endDateMillis != null &&
            budget.isNotBlank() &&
            selectedStyles.isNotEmpty() &&
            step != TripCreateStep.Style

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        CollapsibleSummary(
            visible = showDestinationSummary,
            text = destination,
            onClick = {
                onEditStep(TripCreateStep.Destination)
            }
        )

        CollapsibleSummary(
            visible = showDatesSummary,
            text = formatKoreanDateRange(
                startDateMillis,
                endDateMillis
            ),
            onClick = {
                onEditStep(TripCreateStep.Dates)
            }
        )

        CollapsibleSummary(
            visible = showBudgetSummary,
            text = "${budget}만원",
            onClick = {
                onEditStep(TripCreateStep.Budget)
            }
        )

        CollapsibleSummary(
            visible = showStyleSummary,
            text = tripStyles
                .filter { it in selectedStyles }
                .joinToString(" · "),
            onClick = {
                onEditStep(TripCreateStep.Style)
            }
        )

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (
                    fadeIn() + expandVertically()
                    ) togetherWith (
                    fadeOut() + shrinkVertically()
                    )
            },
            label = "tripCreateStep"
        ) { current ->

            when (current) {

                TripCreateStep.Destination -> {
                    DestinationStep(
                        value = destination,
                        onValueChange = onDestinationChange,
                        onConfirm = onDestinationConfirm
                    )
                }

                TripCreateStep.Dates -> {
                    DatesStep(
                        initialStart = startDateMillis,
                        initialEnd = endDateMillis,
                        onConfirm = onDatesConfirm
                    )
                }

                TripCreateStep.Budget -> {
                    BudgetStep(
                        value = budget,
                        onValueChange = onBudgetChange,
                        onConfirm = onBudgetConfirm
                    )
                }

                TripCreateStep.Style -> {
                    StyleStep(
                        selectedStyles = selectedStyles,
                        onToggle = onStyleToggle
                    )
                }
            }
        }
    }
}

/*
 * ---------------------------------------------------------
 * Preview
 * ---------------------------------------------------------
 */

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun TripCreateScreenPreview() {
    TripCreateScreen()
}
