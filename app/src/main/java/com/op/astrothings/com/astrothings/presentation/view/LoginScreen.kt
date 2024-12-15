package com.op.astrothings.com.astrothings.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.op.astrothings.R
import com.op.astrothings.ui.theme.AstrothingsTheme


@Composable
fun LoginScreen(onLoginClick: (String) -> Unit) {
    val context = LocalContext.current
    val phoneNumber = remember { mutableStateOf("") }

    val isLoginEnabled = remember(phoneNumber.value) {
        phoneNumber.value.length >= 10
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf5f5f5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.astrology),
                contentDescription = stringResource(R.string.app_logo),
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Text
            Text(
                text = stringResource(R.string.welcome_back),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF333333)
            )
            Text(
                text = context.getString(R.string.login_to_your_account),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF777777)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Field
            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                label = { Text(stringResource(R.string.phone_number)) },
                placeholder = { Text(stringResource(R.string.enter_your_registered_phone_number)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = stringResource(R.string.phone_icon)
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { onLoginClick(phoneNumber.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = isLoginEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFFB0C4DE),
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview", widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    AstrothingsTheme {
        LoginScreen(
            onLoginClick = { phoneNumber->
                println("Preview Login Click: $phoneNumber")
            },
        )
    }
}