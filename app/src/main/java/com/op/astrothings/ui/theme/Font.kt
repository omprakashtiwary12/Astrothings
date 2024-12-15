package com.op.astrothings.ui.theme

import android.content.Context
import android.graphics.Typeface
import androidx.compose.runtime.Composable


@Composable
fun getCursiveFont(context: Context): Typeface {
    return Typeface.createFromAsset(context.assets, "fonts/AstroType-Black.ttf")
}