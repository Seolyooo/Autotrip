package com.example.autotrip.ui.plan.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
 * ---------------------------------------------------------
 * 공통 UI
 * ---------------------------------------------------------
 */

@Composable
internal fun StepTitle(text: String) {

    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun CollapsibleSummary(
    visible: Boolean,
    text: String,
    onClick: () -> Unit
) {

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {

        OutlineChoiceCard(
            text = text,
            onClick = onClick
        )
    }
}

@Composable
private fun OutlineChoiceCard(
    text: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(12.dp)
            )
            .clip(
                RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun OutlineInputCard(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    suffix: String = ""
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,

            textStyle = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            ),

            cursorBrush = SolidColor(
                MaterialTheme.colorScheme.primary
            ),

            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,

            modifier = Modifier.fillMaxWidth(),

            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        if (value.isEmpty()) {

                            Text(
                                text = placeholder,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        innerTextField()
                    }

                    if (
                        value.isNotEmpty() &&
                        suffix.isNotEmpty()
                    ) {

                        Text(
                            text = suffix,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        )
    }
}

@Composable
internal fun NextButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}
