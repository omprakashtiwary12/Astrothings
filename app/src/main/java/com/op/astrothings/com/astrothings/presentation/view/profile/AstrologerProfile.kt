package com.op.astrothings.com.astrothings.presentation.view.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.op.astrothings.R
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.Data
import com.op.astrothings.com.astrothings.model.dashboardResponseModel.Reviews
import com.op.astrothings.com.astrothings.util.Constants.Companion.IMG_BASE_URL

enum class MultiFloatingState {
    COLLAPSED,
    EXPANDED
}

data class MiniFabItems(
    val icon: ImageVector,
    val title: String
)

private val headerGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFC107), Color(0xFFFFE082))
)
private val warmOrange = Color(0xFFF57C00)
private val starGold = Color(0xFFFFB300)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstrologerProfile(
    data: Data,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var multiFloatingState by remember { mutableStateOf(MultiFloatingState.COLLAPSED) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val isScrolled = scrollBehavior.state.collapsedFraction > 0.5f
    val topBarTitle = if (isScrolled) data.name else data.name

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Coming Soon!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = dialogMessage,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = isScrolled,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Text(
                            text = topBarTitle ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isScrolled) MaterialTheme.colorScheme.primary else Color(0xFFFFC107),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
            floatingActionButton = {
                FloatingActionButtonScreen(
                    multiFloatingState = multiFloatingState,
                    onStateChange = { state -> multiFloatingState = state }
                )
            },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection) // Enable nested scroll
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {
            item { ProfileHeader(data) }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item { StatsSection(data) }
            item { EnhancedProfileInfo(data) }
            item { EnhancedReviewsSection(data) }
        }
    }
}


@Composable
private fun ProfileHeader(data: Data) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(6.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(headerGradient)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.White, CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = IMG_BASE_URL + data.profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                        .padding(3.dp)
                        .background(Color.Green, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = data.name ?: "",
                fontFamily = FontFamily(Font(R.font.cursive)),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Expert in numerology and career guidance",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.slabo)),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun StatsSection(data: Data) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            value = "${data.experienceYears}+",
            label = "Years"
        )
        StatItem(
            value = "${data.rating}",
            label = "Rating"
        )
        StatItem(
            value = "${data.reviews.size}",
            label = "Reviews"
        )
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily(Font(R.font.slabo)),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )
        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.cursive)),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EnhancedProfileInfo(data: Data) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            InfoSection(
                title = "Education",
                content = data.education ?: "",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Specializations",
                fontFamily = FontFamily(Font(R.font.slabo)),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            FlowRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                data.specializations.forEach { specialization ->

                        Text(
                            text = specialization,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = warmOrange,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.font_jiotype_medium_italic)),
                        )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(
                title = "Languages",
                content = data.languagesKnown.joinToString(", ")
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(
                title = "Location",
                content = data.location ?: ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(
                title = "Availability",
                content = data.availability ?: "Not Available"
            )
        }
    }
}

@Composable
private fun EnhancedReviewsSection(data: Data) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Recent Reviews",
            fontFamily = FontFamily(Font(R.font.slabo)),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        data.reviews.forEach { review ->
            ReviewCard(review)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ReviewCard(review: Reviews) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                RatingBar(
                    rating = review.rating?.toFloat() ?: 5f,
                    starColor = starGold,
                    starSize = 18.dp
                )
                Text(
                    text = "• ${review.reviewDate}",
                    fontFamily = FontFamily(Font(R.font.slabo)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            review.comments?.let {
                Text(
                    text = it,
                    fontFamily = FontFamily(Font(R.font.slabo)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            Text(
                text = review.reviewer ?: "",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: String
) {
    Column {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.slabo)),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black
        )
        Text(
            text = content,
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = FontFamily(Font(R.font.font_jiotype_medium)),
            fontWeight = FontWeight.Light,
            fontSize = 13.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun RatingBar(
    rating: Float,
    starColor: Color,
    starSize: Dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) starColor else Color.LightGray,
                modifier = Modifier.size(starSize),
            )
        }
    }
}
@Composable
fun FloatingActionButtonScreen(
    multiFloatingState: MultiFloatingState,
    onStateChange: (MultiFloatingState) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd // Align the FAB to the bottom end of the screen
    ) {
        // Main Floating Action Button
        FloatingActionButton(
            onClick = {
                onStateChange(
                    if (multiFloatingState == MultiFloatingState.COLLAPSED)
                        MultiFloatingState.EXPANDED
                    else
                        MultiFloatingState.COLLAPSED
                )
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = if (multiFloatingState == MultiFloatingState.EXPANDED)
                    Icons.Default.Close
                else
                    Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }

        // Expanded FAB Options
        AnimatedVisibility(
            visible = multiFloatingState == MultiFloatingState.EXPANDED,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 64.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { /* Handle Option 1 */ },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Email, contentDescription = "Chat")
                }
                SmallFloatingActionButton(
                    onClick = { /* Handle Option 2 */ },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call")
                }
            }
        }
    }
}