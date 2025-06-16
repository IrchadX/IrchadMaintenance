package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.R
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.navigation.Destination
@Composable
fun DeviceCard(modifier: Modifier = Modifier, device: Device, onCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(device.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Left side with icon and information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Device name and icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.device_icon),
                        contentDescription = "Device icon",
                        tint = Color(0xFF2B7A78),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = device.name,
                        color = Color(0xFF2B7A78),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                // Device ID
                Text(
                    text = "ID : ${device.id}",
                    color = Color(0xFF2B7A78),
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 32.dp),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // User name with icon (replaced location)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User icon",
                        tint = Color(0xFF2B7A78),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = device.userName, // Show user's full name instead of location
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

            // Right side with status and image
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Status chip
                StatusChip(status = device.status)

                Spacer(modifier = Modifier.height(12.dp))

                // Device image
                val context = LocalContext.current
                val imageId = remember(device.imageName) {
                    context.resources.getIdentifier(device.imageName, "drawable", context.packageName)
                }

                if (imageId != 0) {
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Device image"
                    )
                } else {
                    // Default image if not found
                    Image(
                        painter = painterResource(id = R.drawable.device),
                        contentDescription = "Default image"
                    )
                }
            }
        }
    }
}
