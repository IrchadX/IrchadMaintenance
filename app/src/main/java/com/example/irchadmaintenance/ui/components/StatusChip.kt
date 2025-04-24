package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight

@Composable
fun StatusChip(modifier : Modifier = Modifier, status : String) {

    val (backgroundColor, textColor) = when(status) {
        "EN SERVICE" -> Pair(Color(0xFFE6FBEC), Color(0xFF008D38))
        "MAINTENANCE" -> Pair(Color(0xFFFFF8E5), Color(0xFFFFB800))
        "EN PANNE" -> Pair(Color(0xFFFEF2F2), Color(0xFFE70000))
        else -> Pair(Color.LightGray, Color.Black)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(2.23.dp)
    ) {

        Text(
            text = status,
            color = textColor,
            fontSize = 7.81.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }

}
