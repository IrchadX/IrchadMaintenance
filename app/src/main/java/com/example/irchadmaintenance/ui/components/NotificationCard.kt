package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.R
import com.example.irchadmaintenance.data.Notification

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit
) {

    val backgroundColor = if (notification.isHandled) {
        Color(0xFFF9F9F9)
    } else {
        Color(0x333AAFA9)
    }

    val indicatorColor = when (notification.severity) {
        "Critique" -> Color(0xFFCC2222)
        "Modéré" -> Color(0xFFFFB800)
        "Mineur" -> Color(0xFF2B7A78)
        else -> {Color(0xFF2B7A78)}
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alert_icon),
                    contentDescription = "Alert Icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF2B7A78)
                )

                if (notification.severity == "Critique" || notification.severity == "Modéré") {

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(indicatorColor, CircleShape)
                            .align(Alignment.BottomEnd)

                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = indicatorColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }

            Text(
                text = notification.timestamp,
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )
        }
    }
}
