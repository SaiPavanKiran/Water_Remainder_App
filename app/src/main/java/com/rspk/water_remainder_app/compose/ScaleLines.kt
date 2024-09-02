package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun ScaleLines(maxProgress: Int, step: Int,modifier: Modifier,value:String) {
    Canvas(modifier = modifier) {
        val sizeMetric = if(value == "PORTRAIT") size.height else size.width
        val lineCount = maxProgress / step

        for (i in 0..lineCount) {
            val position = sizeMetric - (i * (sizeMetric / lineCount))
            if (value == "PORTRAIT") {
                // Draw vertical scale lines for portrait
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = 0f, y = position),
                    end = Offset(x = 20f, y = position),  // Short scale lines
                    strokeWidth = 2f,
                )
            } else {
                // Draw horizontal scale lines for landscape
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = position, y = 0f),
                    end = Offset(x = position, y = 20f),  // Short scale lines
                    strokeWidth = 2f,
                )
            }
        }
    }
}