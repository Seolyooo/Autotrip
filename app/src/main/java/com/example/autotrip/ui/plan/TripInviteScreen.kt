package com.example.autotrip.ui.plan

// Epic: 여행 계획 생성

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autotrip.ui.common.BackTopBar

private enum class CompanionPermission(val label: String) {
    ViewOnly("보기만"),
    ViewAndRecord("보기 + 자기 지출 기록")
}

@Composable
fun TripInviteScreen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    // TODO: 초대 API 연결 전까지 사용하는 임시 코드
    val inviteCode = "OSK-1234"

    var permission by remember { mutableStateOf(CompanionPermission.ViewAndRecord) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    fun shareCode() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "AutoTrip 여행에 함께해요! 초대 코드: $inviteCode")
        }
        context.startActivity(Intent.createChooser(intent, "초대 코드 공유"))
    }

    fun copyCode() {
        clipboard.setText(AnnotatedString(inviteCode))
        Toast.makeText(context, "코드를 복사했어요", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "친구 초대",
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

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "가족이나 친구를 초대해보세요!",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))

            InviteCard(
                inviteCode = inviteCode,
                permission = permission,
                onPermissionChange = { permission = it },
                onShareClick = ::shareCode,
                onCopyClick = ::copyCode
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "지금 초대하지 않아도 돼요.\n'다음'을 누르면 건너뛸 수 있어요.",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 8.dp
                    )
                    .height(56.dp)
            ) {
                Text(
                    text = "다음",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun InviteCard(
    inviteCode: String,
    permission: CompanionPermission,
    onPermissionChange: (CompanionPermission) -> Unit,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "코드로 초대",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = inviteCode,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.IosShare,
                    contentDescription = "초대 코드 공유"
                )
            }

            IconButton(onClick = onCopyClick) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "초대 코드 복사"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "동행인 권한",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        CompanionPermission.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = permission == option,
                        onClick = { onPermissionChange(option) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = permission == option,
                    onClick = null
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = option.label,
                    fontSize = 15.sp
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
private fun TripInviteScreenPreview() {
    TripInviteScreen()
}
