package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.R
import com.example.irchadmaintenance.data.Device

@Composable
fun DeviceCard(modifier: Modifier = Modifier, device: Device, onCardClick: (String) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(device.id.toString()) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 18.dp, horizontal = 17.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = device.name,
                        color = Color(0xFF2B7A78),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.15.sp
                    )
                }
                Text(
                    text = "ID : ${device.id}",
                    color = Color(0xFF2B7A78),
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 24.dp),
                    fontSize = 11.15.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Type : ${device.type}",
                    color = Color.Black,
                    fontSize = 11.15.sp,
                    modifier = Modifier.padding(start = 24.dp)
                )
                Text(
                    text = "Version : ${device.softwareVersion}",
                    color = Color.Black,
                    fontSize = 11.15.sp,
                    modifier = Modifier.padding(start = 24.dp)
                )
                Text(
                    text = "MAC : ${device.macAddress}",
                    color = Color.Black,
                    fontSize = 11.15.sp,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }

            Spacer(modifier = Modifier.width(60.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                StatusChip(Modifier, device.status)
                Spacer(modifier = Modifier.height(13.dp))
                Image(
                    painter = painterResource(id = R.drawable.device_icon),
                    contentDescription = device.name,
                    modifier = Modifier
                        .width(91.44.dp)
                        .height(63.56.dp)
                        .clip(RoundedCornerShape(2.23.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
