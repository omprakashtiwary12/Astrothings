package com.op.astrothings.com.astrothings.presentation.view.login

import DimmingProgressBarOverlay
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.op.astrothings.R
import com.op.astrothings.com.astrothings.data.model.requestJSON.RequestOTPJson
import com.op.astrothings.com.astrothings.navigation.Screen
import com.op.astrothings.com.astrothings.viewmodels.LoginViewModel
import com.op.astrothings.ui.theme.BlueGray
import com.op.astrothings.ui.theme.screenBackend
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel) {
    val context = LocalContext.current
    val tnc = "Terms and Condition"
    val privacyPolicy = "Privacy policy"
    val isLoading = remember { mutableStateOf(false) }
    val phoneNumber = remember { mutableStateOf("") }
    val systemUiController = rememberSystemUiController()
    val isLoginEnabled = remember(phoneNumber.value) {
        phoneNumber.value.length >= 10
    }
    val loginState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color(211, 145, 11, 255),
            darkIcons = false
        )
    }

    val requestOTPJson = remember(phoneNumber.value) { RequestOTPJson(phoneNumber.value) }

    when (val state = loginState.value) {
        is LoginState.Loading -> {
            isLoading.value = true
            viewModel.resetState()
        }
        is LoginState.RequestOTPSuccess -> {
            isLoading.value = false
            val number = phoneNumber.value
            navController.navigate(Screen.Otp.createRoute(number)) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            viewModel.resetState()
        }
        is LoginState.RequestOTPAPIError -> {
            isLoading.value = false
            viewModel.resetState()
            Toast.makeText(context, "Error: ${state.errorResponseModel.error}", Toast.LENGTH_SHORT).show()
        }
        is LoginState.RequestOTPNetworkError -> {
            isLoading.value = false
            viewModel.resetState()
            Toast.makeText(context, "Network Error: ${state.error}", Toast.LENGTH_SHORT).show()
        }
        is LoginState.RequestOTPUnknownError -> {
            isLoading.value = false
            viewModel.resetState()
            Toast.makeText(context, "Unknown Error: ${state.error}", Toast.LENGTH_SHORT).show()
        }
        else -> Unit
    }

    DimmingProgressBarOverlay(showProgressBar = isLoading.value) {
        val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopSection()
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { input ->
                        val validatedInput = input.filter { it.isDigit() }
                        if (validatedInput.isEmpty() || (validatedInput.length <= 10 && validatedInput.matches(
                                Regex("^[6-9].*")
                            ))
                        ) {
                            phoneNumber.value = validatedInput
                        } else if (!phoneNumber.value.matches(Regex("^[6-9][0-9]{0,9}$"))) {
                            Toast.makeText(context, "Invalid Phone Number", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.phone_number),
                            color = uiColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.enter_your_registered_phone_number),
                            color = if (isSystemInDarkTheme()) uiColor else Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = stringResource(R.string.phone_icon),
                            tint = if (isSystemInDarkTheme()) uiColor else Color.White
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 20.sp, // Set the desired font size here
                        letterSpacing = 2.sp, // Add letter spacing here
                        color = if (isSystemInDarkTheme()) uiColor else Color.White
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else screenBackend,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) Color.White else screenBackend,
                        cursorColor = if (isSystemInDarkTheme()) uiColor else Color.White,
                        focusedTextColor = if (isSystemInDarkTheme()) uiColor else Color.White,
                        unfocusedTextColor = if (isSystemInDarkTheme()) uiColor else Color.White,
                        focusedContainerColor = if (isSystemInDarkTheme()) BlueGray else screenBackend,
                        unfocusedContainerColor = if (isSystemInDarkTheme()) BlueGray else screenBackend
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                val isChecked = remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { isChecked.value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = uiColor,
                            uncheckedColor = uiColor
                        )
                    )
                    val annotatedString = buildAnnotatedString {
                        val prefixColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        val linkColor = screenBackend // Assuming `screenBackend` is used for link text color.

                        withStyle(style = SpanStyle(color = prefixColor)) {
                            append("I have read ")
                        }
                        withStyle(style = SpanStyle(color = linkColor)) {
                            pushStringAnnotation(tag = tnc, annotation = tnc)
                            append(tnc)
                        }
                        withStyle(style = SpanStyle(color = prefixColor)) {
                            append(" and ")
                        }
                        withStyle(style = SpanStyle(color = linkColor)) {
                            pushStringAnnotation(tag = privacyPolicy, annotation = privacyPolicy)
                            append(privacyPolicy)
                        }
                    }
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(start = offset, end = offset)
                                .firstOrNull()?.let { span ->
                                    if (span.item == tnc) {
                                        navController.navigate(
                                            "webview_screen/${
                                                URLEncoder.encode(
                                                    "https://kamleshkhyatiinfosolution.com/term-condition.php",
                                                    "UTF-8"
                                                )
                                            }"
                                        )
                                    } else if (span.item == privacyPolicy) {
                                        navController.navigate(
                                            "webview_screen/${
                                                URLEncoder.encode(
                                                    "https://kamleshkhyatiinfosolution.com/privacy-policy.php",
                                                    "UTF-8"
                                                )
                                            }"
                                        )
                                    }
                                }
                        }
                    )
                }


                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (!isChecked.value) {
                            Toast.makeText(
                                context,
                                "Please accept the Privacy Policy and T&Cs",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.viewModelScope.launch {
                                viewModel.loginIntent.send(
                                    LoginIntent.GenerateLoginOTP(requestOTPJson)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth().padding(12.dp)
                        .height(50.dp),
                    enabled = isLoginEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color.White else screenBackend,
                        contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        disabledContainerColor = Color(0x6066717C),
                        disabledContentColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isSystemInDarkTheme()) Color(211, 145, 11, 255) else Color.White
                    )
                }
            }
        }
        }

}


@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Box(contentAlignment = Alignment.TopCenter) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    fontFamily = FontFamily(Font(R.font.cursive)),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}