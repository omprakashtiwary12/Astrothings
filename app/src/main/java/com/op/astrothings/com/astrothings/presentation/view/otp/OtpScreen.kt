package com.op.astrothings.com.astrothings.presentation.view.otp


import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.op.astrothings.R
import com.op.astrothings.com.astrothings.composables.OtpTextField
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPVerifyJson
import com.op.astrothings.com.astrothings.presentation.view.login.LoginIntent
import com.op.astrothings.com.astrothings.viewmodels.LoginViewModel
import com.op.astrothings.com.astrothings.viewmodels.OtpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtpScreen(
    navController: NavHostController,
    phoneNumber: String,
    viewModel: OtpViewModel,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val otpState by viewModel.state.collectAsState()
    var timer by remember { mutableIntStateOf(30) }
    val systemUiController = rememberSystemUiController()
    var isResendEnabled by remember { mutableStateOf(false) }
    val otpCode = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    // Initialize system UI only once when the composable is first created
    LaunchedEffect(Unit) {
        delay(300)
        systemUiController.setSystemBarsColor(
            color = Color(211, 145, 11, 255), // Golden Yellow
            darkIcons = false
        )
    }

    // Handle Timer and Resend logic
    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000L)
            timer--
        } else {
            isResendEnabled = true
        }
    }

    // Update loading state based on OTP verification state
    LaunchedEffect(otpState) {
        when (val state = otpState) {
            is OTPVerifyState.Loading -> {
                isLoading.value = true
                viewModel.resetState()
            }
            is OTPVerifyState.VerifyOTPSuccess -> {
                isLoading.value = false

                val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                navController.navigate("dashboard") {
                   // popUpTo("otp") { inclusive = true }
                }
                viewModel.resetState()
            }
            is OTPVerifyState.VerifyOTPAPIError -> {
                isLoading.value = false
                viewModel.resetState()
                Toast.makeText(context, "Error: ${state.errorResponseModel.error}", Toast.LENGTH_SHORT).show()
            }
            is OTPVerifyState.VerifyOTPNetworkError -> {
                isLoading.value = false
                viewModel.resetState()
                Toast.makeText(context, "Network Error: ${state.error}", Toast.LENGTH_SHORT).show()
            }
            is OTPVerifyState.VerifyOTPUnknownError -> {
                isLoading.value = false
                viewModel.resetState()
                Toast.makeText(context, "Unknown Error: ${state.error}", Toast.LENGTH_SHORT).show()
            }
        else -> Unit
        }
    }

    // Wrap content with Box to show progress indicator over everything
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(211, 145, 11, 255)), // Golden Yellow
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.otp_verification),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Text(
                text = "Enter the OTP sent to $phoneNumber",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OtpTextField(
                value = otpCode.value,
                length = 4,
                onValueChange = { otpCode.value = it },
                onVerificationExplicitlyTriggered = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (timer > 0) "Resend OTP in $timer seconds" else "You can resend OTP now",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isResendEnabled) {
                        val requestOTPJson = RequestOTPJson(phoneNumber)
                        loginViewModel.viewModelScope.launch {
                            loginViewModel.loginIntent.send(LoginIntent.GenerateLoginOTP(requestOTPJson))
                        }
                        timer = 30
                        isResendEnabled = false
                    }
                },
                enabled = isResendEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(211, 145, 11, 255),
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                )
            ) {
                Text("Resend OTP")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.otpIntent.send(
                            OtpIntent.VerifyLoginOTP(RequestOTPVerifyJson(phoneNumber, otpCode.value))
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(211, 145, 11, 255)
                )
            ) {
                Text("Verify OTP")
            }
        }

        // Dimming progress bar overlay that only shows during loading
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
