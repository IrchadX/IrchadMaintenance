package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.data.SampleData


@Composable
fun DeviceInfoList(deviceId : String) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val device = SampleData.devices.find {it.id == deviceId}
        if (device != null) {
            Column (){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "ID de l'appareil",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.id,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Type",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.type,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Statut",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.status,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "ID de l'utilisateur",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.userId,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Adresse MAC",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.macAddress,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Version du logiciel",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.softwareVersion,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Date d'activation",
                        color = Color(0xFF3AAFA9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = device.activationDate,
                        color = Color(0xFFAAA3A3),
                        fontSize = 20.sp)
                }
            }
        } else {
            Text(text = "No device with this matching id found")
        }

    }
}