package com.op.astrothings.com.astrothings.presentation.view.dashboard

import DimmingProgressBarOverlay
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.op.astrothings.R
import com.op.astrothings.com.astrothings.model.NavigationItems
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.Data
import com.op.astrothings.com.astrothings.navigation.Screen
import com.op.astrothings.com.astrothings.util.Constants.Companion.IMG_BASE_URL
import com.op.astrothings.ui.theme.customTypography
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URLEncoder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val cardColor = Color(255, 255, 255, 255)
val backgroundColor = Color(245, 245, 245, 255)

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController, viewModel: DashboardViewModel) {
    val isLoading = remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }
    val items = listOf(
        NavigationItems(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home),
        NavigationItems(title = "Info", selectedIcon = Icons.Filled.Info, unselectedIcon = Icons.Outlined.Info),
        NavigationItems(title = "Edit", selectedIcon = Icons.Filled.Edit, unselectedIcon = Icons.Outlined.Edit, badgeCount = 105),
        NavigationItems(title = "Settings", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings),
        NavigationItems(title = "PrivacyPolicy", selectedIcon = Icons.Filled.Person, unselectedIcon = Icons.Outlined.Person)
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    LaunchedEffect(Unit) {
        viewModel.dashboardIntent.send(DashboardIntent.GetAstrologers)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val dashboardState = viewModel.state.collectAsState()
    var astrologers = ArrayList<Data>()
    when (val state = dashboardState.value) {
        is DashboardState.Loading -> { isLoading.value = true }

        is DashboardState.DashboardSuccess -> { isLoading.value = false
            astrologers = state.dashboardResponseModel.data
            Timber.tag("DashboardScreen").e("Dashboard Success: %s", astrologers)
            AstrologerList(astrologers,navController)

        }

        is DashboardState.DashboardAPIError -> { isLoading.value = false }

        is DashboardState.DashboardNetworkError -> { isLoading.value = false }

        is DashboardState.DashboardUnknownError -> { isLoading.value = false }

        else -> {}
    }

    DimmingProgressBarOverlay(showProgressBar = isLoading.value) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            scrimColor = cardColor,
            drawerContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(colors = listOf(Color(0xFFBBDEFB), Color(0xFFE3F2FD)))) // Gradient background
                ) {
                    ModalDrawerSheet {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Person image holder with pencil icon
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                // Profile Image
                                Image(
                                    painter = painterResource(id = R.drawable.op), // Replace with your image resource
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                )
                            }

                            // Pencil icon positioned exactly on the radius
                            Icon(
                                imageVector = Icons.Default.Edit, // Pencil icon
                                contentDescription = "Edit Profile",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-107).dp, y = (-67).dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        navController.navigate("profile") {
                                            popUpTo("dashboard") { inclusive = true }
                                        }
                                    }
                                    .padding(4.dp),
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        items.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (index == selectedItemIndex) FontWeight.Bold else FontWeight.Normal,
                                            color = if (index == selectedItemIndex) Color(0xFF6200EE) else Color.Black
                                        )
                                    )
                                },
                                selected = index == selectedItemIndex,
                                onClick = {
                                    if (item.title == "PrivacyPolicy") {
                                        navController.navigate("webview_screen/${URLEncoder.encode("https://kamleshkhyatiinfosolution.com/privacy-policy.php", "UTF-8")}")
                                    }
                                    selectedItemIndex = index
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title,
                                        tint = if (index == selectedItemIndex) Color(0xFF6200EE) else Color.Gray
                                    )
                                },
                                badge = {
                                    item.badgeCount?.let {
                                        Text(text = it.toString())
                                    }
                                },
                                modifier = Modifier
                                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    .background(
                                        if (index == selectedItemIndex) Color(0xFFE1BEE7) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp) // Add padding for better touch target
                            )
                        }
                    }
                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Chat With Astrologer") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFFFD700),
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black
                        )
                    )
                },
                containerColor = cardColor,
                content = { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        AstrologerList(astrologers, navController)
                    }
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AstrologerList(astrologers: ArrayList<Data>,navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        items(astrologers.size) { index ->
            AstrologerCard(
                name = astrologers[index].name ?: "",
                languages = astrologers[index].languagesKnown.joinToString(", "),
                experience = astrologers[index].experienceYears.toString() + " years of experience",
                availability = astrologers[index].availability ?: "",
                rating = astrologers[index].rating ?: 0.0,
                imageProfile = (IMG_BASE_URL + astrologers[index].profilePictureUrl),
                time = Instant.now(),
                onClick = { onAstrologerClick(astrologers[index], navController) }

            )
            print("ImageUrl: ${astrologers[index].profilePictureUrl}")
        }
    }
}

fun onAstrologerClick(data: Data, navController: NavController) {
    navController.navigate(Screen.AstrologerProfile.createRoute(data))
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AstrologerCard(
    name: String,
    languages: String,
    experience: String,
    availability: String,
    rating: Double,
    imageProfile: String,
    time: Instant,
    onClick: () -> Unit
) {
    var isSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(cardColor)
    ) {
        Column {
            CardHeader(isAvailable = availability, time = time)

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberAsyncImagePainter(model = imageProfile)

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFFFD700), CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Astrologer details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.cursive)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = languages,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily(Font(R.font.slabo)),
                        color = Color.Black
                    )
                    Text(
                        text = experience,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily(Font(R.font.slabo)),
                        color = Color.Black
                    )
                    Text(
                        text = availability,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily(Font(R.font.slabo)),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
                    val scope = rememberCoroutineScope()
                    OutlinedButton(
                        onClick = {  scope.launch {
                            sheetState.show()
                        }.invokeOnCompletion {
                            isSheetVisible = true
                        }},
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF00C853)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF00C853)), // Green border
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(36.dp)
                    ) {
                        Text(
                            text = "Chat",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily(Font(R.font.slabo)),
                        )
                    }
            }
        }
    }
    if (isSheetVisible) {
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    isSheetVisible = false
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFFFFD700)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Payment feature is in progress, coming soon...", color = Color.Black, style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize))
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardHeader(isAvailable: String, time: Instant) {
    val headerColor = if (isAvailable == "Available") Color(0xFF00C853) else Color(0xFFD32F2F) // Green for online, red for offline
    val statusText = if (isAvailable == "Available") "Available" else "Offline"
    val statusIcon = if (isAvailable == "Available") R.drawable.ic_online else R.drawable.ic_offline // Replace with your actual icon resources

    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = statusIcon),
                contentDescription = "Status Icon",
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = statusText,
                color = Color.White, // Text color for better contrast
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = formatter.format(time),
            color = Color.White, // Text color for better contrast
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}


