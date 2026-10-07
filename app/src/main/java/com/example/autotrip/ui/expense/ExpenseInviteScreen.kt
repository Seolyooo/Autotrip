package com.example.autotrip.ui.expense

// 12 함께보기 초대

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autotrip.data.sample.SampleInviteMember
import com.example.autotrip.data.sample.SampleInvitePreviewLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseInviteScreen(
    state: ExpenseInviteUiState,
    onBack: () -> Unit,
) {
    // 의도: 동행인 권한 선택은 화면 안 상태. 저장하지 않음
    var selectedPermissionIndex by remember(state) { mutableIntStateOf(state.defaultPermissionIndex) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("함께보기 · ${state.tripTitle}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "링크로 초대",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(Icons.Filled.Link, contentDescription = null)
                        Text(state.inviteLinkText, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        // 임시: 클립보드 복사는 다음 단계에서 연결 (지금은 자리만)
                        OutlinedButton(onClick = {}) { Text("복사") }
                    }
                    Text(
                        "초대 코드 [${state.inviteCodeText}] · ${state.inviteExpireText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("동행인 권한", style = MaterialTheme.typography.titleMedium)
                state.permissionOptions.forEachIndexed { index, option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPermissionIndex == index,
                            onClick = { selectedPermissionIndex = index },
                        )
                        Text(option, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("멤버", style = MaterialTheme.typography.titleMedium)
                    Text(
                        state.memberCountText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                HorizontalDivider()
                state.members.forEachIndexed { index, member ->
                    InviteMemberRow(member)
                    // 의도: 이미지처럼 항목 사이에만 구분선을 두고, 마지막 항목 뒤에는 두지 않음
                    if (index != state.members.lastIndex) HorizontalDivider()
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("동행인에게 보이는 화면", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider()
                InvitePreviewCard(
                    memberName = state.preview.memberName,
                    lines = state.preview.lines,
                    actionButtonText = state.preview.actionButtonText,
                    hintText = state.preview.hintText,
                )
            }
        }
    }
}

// 임시: 사람 아바타 대신(U5 결정과 동일) 이름 앞글자 원형 배지로 표시
@Composable
private fun InviteMemberRow(member: SampleInviteMember) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MemberInitialBadge(member.initial)
        Column {
            Text(member.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                member.subtitleText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun InvitePreviewCard(
    memberName: String,
    lines: List<SampleInvitePreviewLine>,
    actionButtonText: String,
    hintText: String,
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("${memberName}님 화면", style = MaterialTheme.typography.bodyMedium)
            lines.forEach { line ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${line.toName}에게", style = MaterialTheme.typography.bodyLarge)
                    Text(line.amountText, style = MaterialTheme.typography.titleMedium)
                }
            }
            // 임시: '보냈어요' 확인 요청은 다음 단계에서 연결 (지금은 자리만)
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text(actionButtonText) }
            Text(
                hintText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// 임시: onClick 없이 보여주기만 함 (ExpenseSettlementScreen과 동일한 관례)
@Composable
private fun MemberInitialBadge(initial: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            initial,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseInviteScreenPreview() {
    ExpenseInviteScreen(
        state = ExpenseInviteUiState(),
        onBack = {},
    )
}
