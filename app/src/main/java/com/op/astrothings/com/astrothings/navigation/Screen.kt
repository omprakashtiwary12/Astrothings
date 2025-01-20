package com.op.astrothings.com.astrothings.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.Data
import com.op.astrothings.com.astrothings.presentation.view.SplashScreen
import com.op.astrothings.com.astrothings.presentation.view.dashboard.DashboardScreen
import com.op.astrothings.com.astrothings.presentation.view.dashboard.DashboardViewModel
import com.op.astrothings.com.astrothings.presentation.view.login.LoginScreen
import com.op.astrothings.com.astrothings.presentation.view.login.WebViewScreen
import com.op.astrothings.com.astrothings.presentation.view.otp.OtpScreen
import com.op.astrothings.com.astrothings.presentation.view.profile.AstrologerProfile
import com.op.astrothings.com.astrothings.util.ProfileCard
import com.op.astrothings.com.astrothings.util.toData
import com.op.astrothings.com.astrothings.util.toJson
import com.op.astrothings.com.astrothings.viewmodels.LoginViewModel
import com.op.astrothings.com.astrothings.viewmodels.OtpViewModel

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Otp : Screen("otp/{mobileNumber}") {
        fun createRoute(mobileNumber: String) = "otp/$mobileNumber"
    }
    data object Dashboard : Screen("dashboard")
    data object Profile : Screen("profile")
    data object AstrologerProfile : Screen("astrologer_profile") {
        fun createRoute(data: Data): String {
            return "astrologer_profile/${Uri.encode(data.toJson())}"
        }
    }
    data object Webview : Screen("webview_screen/{url}")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationStack() {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val otpViewModel: OtpViewModel = hiltViewModel()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) {
            LoginScreen(navController, viewModel = loginViewModel)
        }
        composable(
            Screen.Otp.route,
            arguments = listOf(navArgument("mobileNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val mobileNumber = backStackEntry.arguments?.getString("mobileNumber") ?: ""
            OtpScreen(navController,mobileNumber, viewModel = otpViewModel, loginViewModel = loginViewModel)
        }
        composable(Screen.Dashboard.route) { DashboardScreen(navController,viewModel = dashboardViewModel) }
        composable(
            Screen.AstrologerProfile.route + "/{astrologerData}",
            arguments = listOf(navArgument("astrologerData") { type = NavType.StringType })
        ) { backStackEntry ->
            val astrologerDataJson = backStackEntry.arguments?.getString("astrologerData") ?: ""
            val astrologerData = astrologerDataJson.toData()
            AstrologerProfile(astrologerData, navController)
        }
        composable(Screen.Profile.route) { ProfileCard(navController) }
        composable(
            Screen.Webview.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(navController, url)
        }
    }
}



