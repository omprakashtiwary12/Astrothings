package com.op.astrothings.com.astrothings.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar() {
    val transition = rememberInfiniteTransition()

    // Create rotation animation
    val rotation = transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Number of bars
    val numberOfBars = 12

    androidx.compose.foundation.Canvas(modifier = Modifier.size(150.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Calculate the angle between each bar
        val angleStep = 360f / numberOfBars

        val radius = size.minDimension / 2.2f
        val barWidth = 12.dp.toPx()

        // Draw animated bars
        for (i in 0 until numberOfBars) {
            val angle = i * angleStep + rotation.value
            val alpha = 1f - (i.toFloat() / numberOfBars)
            rotate(degrees = angle, pivot = Offset(canvasWidth / 2, canvasHeight / 2)) {
                drawRoundRect(
                    color = Color.Blue.copy(alpha = alpha),
                    topLeft = Offset(canvasWidth / 2 - barWidth / 2, canvasHeight / 2 - radius),
                    size = Size(barWidth, radius / 2),
                    cornerRadius = CornerRadius(x = barWidth / 2, y = barWidth / 2)
                )
            }
        }
    }
}