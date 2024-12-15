package com.op.astrothings.com.astrothings.presentation.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.op.astrothings.R
import com.op.dailydiary.ui.theme.DisappointedColor
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreenWithWritingPencil() {
    val text = "Welcome To AstroThings"
    val textLength = text.length
    var textProgress by remember { mutableFloatStateOf(0f) }
    val animatedTextProgress by animateFloatAsState(
        targetValue = textProgress,
        animationSpec = tween(durationMillis = 5000)
    )
    LaunchedEffect(Unit) {
        for (i in 1..textLength) {
            textProgress = i.toFloat()
            delay(100)
        }
    }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val logo: Painter = painterResource(id = R.drawable.astrology)
            Image(
                painter = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Writing animation box
            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.BottomStart) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 24.dp)
                ) {
                    val textPaint = android.graphics.Paint().apply {
                        color = DisappointedColor.toArgb()
                        textSize = 64f
                    }

                    val visibleText = text.substring(0, animatedTextProgress.toInt().coerceAtMost(textLength))
                    val textWidth = textPaint.measureText(visibleText)
                    val canvasWidth = size.width
                    val xOffset = (canvasWidth - textWidth) / 2
                    FontFamily(Font(R.font.font_jiotype_bold))
                    drawContext.canvas.nativeCanvas.drawText(
                        visibleText,
                        xOffset,
                        size.height / 2,
                        textPaint,


                    )
                }
                val pencilPositionPx = with(density) { 18.dp.toPx() } * animatedTextProgress
                val pencilOffset = with(density) { pencilPositionPx.toDp() }
                Image(
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = "Pencil",
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = pencilOffset, y = 0.dp)
                )
            }
        }
    }
}