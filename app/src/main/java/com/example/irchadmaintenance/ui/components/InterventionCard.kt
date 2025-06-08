package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.data.models.Intervention

@Composable
fun InterventionCard(
    intervention: Intervention,
    onCardClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCardClick(intervention.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Intervention icon",
                        tint = Color(0xFF2B7A78),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = intervention.title?.takeIf { it.isNotEmpty() } ?: "Intervention sans titre",
                        color = Color(0xFF2B7A78),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Date: ${intervention.scheduledDate}",
                    color = Color(0xFF2B7A78),
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 28.dp),
                    fontSize = 12.sp
                )

                if (intervention.location.isNotEmpty()) {
                    Text(
                        text = "Lieu: ${intervention.location}",
                        color = Color(0xFF2B7A78),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(start = 28.dp),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = intervention.description?.takeIf { it.isNotEmpty() } ?: "Pas de description",
                    color = Color.Black,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            InterventionStatusChip(status = intervention.status)
        }
    }
}

@Composable
fun InterventionStatusChip(status: String) {
    val (backgroundColor, textColor, text) = when(status.lowercase()) {
        "completed" -> Triple(Color(0xFFE6FBEC), Color(0xFF008D38), "Terminé")
        "pending" -> Triple(Color(0xFFFFF8E5), Color(0xFFFFB800), "En cours")
        "in_progress" -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "En cours")
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF757575), status.replaceFirstChar { it.uppercase() })
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}