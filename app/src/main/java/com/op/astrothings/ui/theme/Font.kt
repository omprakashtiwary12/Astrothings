package com.op.astrothings.ui.theme

import android.content.Context
import android.graphics.Typeface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.op.astrothings.R


@Composable
fun getCursiveFont(context: Context): Typeface {
    return Typeface.createFromAsset(context.assets, "fonts/AstroType-Black.ttf")
}
val cursiveFontFamily = FontFamily(
    Font(R.font.cursive, FontWeight.Normal)
)

val customTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = cursiveFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )
)