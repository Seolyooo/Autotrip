package com.example.autotrip.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 회원가입 화면. 레이아웃/버튼 스케치 단계라 유효성 검사, API 연동은 다음 단계에서 붙임.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onSignUpComplete: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var referralChecked by remember { mutableStateOf(false) }
    var referralCode by remember { mutableStateOf("") }

    var marketingChecked by remember { mutableStateOf(false) }

    var termsExpanded by remember { mutableStateOf(false) }
    var termsAgreed by remember { mutableStateOf(false) }

    // 필수 항목 다 채워야 버튼 활성화 (스케치 단계 임시 조건, 정책 확정되면 조정)
    val isSignUpEnabled = email.isNotBlank() &&
            password.isNotBlank() &&
            name.isNotBlank() &&
            phone.isNotBlank() &&
            termsAgreed

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {},
                actions = {
                    IconButton(onClick = { /* TODO: 고객센터 연결 */ }) {
                        Icon(Icons.Default.HeadsetMic, contentDescription = "고객센터")
                    }
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email@example.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호를 입력해주세요") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름을 입력해주세요") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("휴대폰 번호를 -없이 입력해주세요") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 추천인 코드 (선택)
            OptionRow(
                label = "추천인 아이디 / 프로모션 코드 (선택)",
                checked = referralChecked,
                onCheckedChange = { referralChecked = it }
            )
            if (referralChecked) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = referralCode,
                    onValueChange = { referralCode = it },
                    label = { Text("추천인 아이디 / 코드 입력") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 마케팅 수신 동의 (선택)
            OptionRow(
                label = "신제품, 이벤트 안내 등 광고성 마케팅 수신 동의 (선택)",
                checked = marketingChecked,
                onCheckedChange = { marketingChecked = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 이용약관 (필수, 펼쳐보기)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { termsExpanded = !termsExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "오토트립다이어리 이용약관",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ExpandMore, contentDescription = "펼치기")
                }

                if (termsExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "여기에 실제 이용약관 전문이 들어갑니다. (추후 법무 검토본으로 교체)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OptionRow(
                    label = "본인은 만 14세 이상이며, 위 약관 내용을 확인하였습니다.",
                    checked = termsAgreed,
                    onCheckedChange = { termsAgreed = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignUpComplete,
                enabled = isSignUpEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("동의하고 회원가입")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen()
}
