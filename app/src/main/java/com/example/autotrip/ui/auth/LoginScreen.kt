package com.example.autotrip.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
    로그인 방법 선택 화면
    - Google / email / 회원가입
*/
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onEmailLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AutoTrip",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(60.dp))

        // 1. Google login
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Google로 로그인")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. email login
        Button(
            onClick = onEmailLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("이메일로 로그인")
        }

        // 3. 회원가입
        TextButton(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
        ) {
            Text("회원가입")
        }
    }
}