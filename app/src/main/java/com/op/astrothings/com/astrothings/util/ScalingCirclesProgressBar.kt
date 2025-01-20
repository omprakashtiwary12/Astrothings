import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DimmingProgressBarOverlay(showProgressBar: Boolean, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        content()

        if (showProgressBar) {
            // Dimmed overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                ScalingCirclesProgressBar() // Centered progress bar
            }
        }
    }
}

@Composable
fun ScalingCirclesProgressBar(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()

    // Animation durations and delays
    val duration = 500
    val delays = listOf(0, 100, 200)

    // Create animations for each circle
    val scales = delays.map { delay ->
        transition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    delayMillis = delay,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
    }

    // Layout with 3 circles
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.size(150.dp)
    ) {
        scales.forEach { scale ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}