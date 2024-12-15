package com.op.astrothings

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.op.astrothings.com.astrothings.presentation.view.LoginScreen
import com.op.astrothings.com.astrothings.presentation.view.SplashScreenWithWritingPencil
import com.op.astrothings.ui.theme.AstrothingsTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AstrothingsTheme {
                MainAppContent()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainAppContent() {
    var showSplash by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        showSplash = false
    }

    if (showSplash) {
        SplashScreenWithWritingPencil()
    } else {
        LoginScreen(
            onLoginClick = { phoneNumber ->
                println("Logging in with email: $phoneNumber")
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AstrothingsTheme {

    }
}