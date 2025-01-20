package com.op.astrothings.com.astrothings.util

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProfileCard(navController: NavHostController) {
    BackHandler {
        navController.navigate("dashboard")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(211, 145, 11, 255))
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("dashboard") }) {
                Icon(
                    painter = painterResource(id = com.op.astrothings.R.drawable.ic_back),
                    contentDescription = "Back Icon",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.size(50.dp)) {
                Icon(
                    painter = painterResource(id = com.op.astrothings.R.drawable.ic_notifications_white_24dp),
                    contentDescription = "Notification Icon",
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(id = com.op.astrothings.R.drawable.astrology),
                    contentDescription = "Notification Badge",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Combined card for all sections
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(120.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Image(
                        painter = painterResource(id = com.op.astrothings.R.drawable.op),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RectangleShape)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Profile Info
                Text(
                    text = "Om Prakash Tiwari",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242)
                )
                Text(
                    text = "Software Engineer",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gender Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Gender",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                    var selectedGender by remember { mutableStateOf("Male") }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedGender == "Male",
                            onClick = { selectedGender = "Male" }
                        )
                        Text(text = "Male", fontSize = 16.sp, color = Color(0xFF424242))
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = selectedGender == "Female",
                            onClick = { selectedGender = "Female" }
                        )
                        Text(text = "Female", fontSize = 16.sp, color = Color(0xFF424242))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date of Birth
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Date of Birth",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                    var selectedDate by remember { mutableStateOf("01/01/1990") }
                    Text(
                        text = selectedDate,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time of Birth
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Time of Birth",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                    var selectedTime by remember { mutableStateOf("05:30 AM") }
                    Text(
                        text = selectedTime,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Place of Birth
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Place of Birth",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                    var selectedPlace by remember { mutableStateOf("Sitamarhi, Bihar, India") }
                    Text(
                        text = selectedPlace,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}




