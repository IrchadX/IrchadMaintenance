package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.ui.components.AppHeader
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.irchadmaintenance.viewmodels.NotificationsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.OSMDroidMap

@Composable
fun NotificationDetailsScreen(
    notificationId: Int,
    userId: String,
    deviceId: Int,
    navController: NavController
) {
    val viewModel: NotificationsViewModel = viewModel()
    val notification = viewModel.notifications.find { it.id == notificationId }
    val user = UserSampleData.users.find { it.userId == userId }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            user,
            navController,
            "Alertes",
            false,
            false
        )

        Spacer(modifier = Modifier.height(15.dp))

        //////////////// making sure the notification exists before rendering its information //////////

        notification?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(48.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ID de l'appareil",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3AAFA9)
                        )

                        Text(
                            text = deviceId.toString(),
                            fontSize = 20.sp,
                            color = Color(0xFFAAA3A3)
                        )
                    }

                    Spacer(modifier = Modifier.height(34.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Type d'alerte",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3AAFA9)
                        )

                        Text(
                            text = notification.alertType,
                            fontSize = 20.sp,
                            color = Color(0xFFAAA3A3)
                        )
                    }

                    Spacer(modifier = Modifier.height(34.dp))

                    DescriptionBox(notification.message)

                    Spacer(modifier = Modifier.height(42.dp))

                    Button(
                        onClick = {
                            navController.navigate(
                                Destination.Interventions.createRoute(userId)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF17252A),
                            contentColor = Color(0xFFFCFFFE)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
                    ) {
                        Text(
                            text = "Intervenir",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(124.dp))
                }
            }
        } ?: Text(text = "Notification not found")
    }
}

@Composable
fun DescriptionBox(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Description",
                color = Color(0xFF3AAFA9),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(Color(0xFFAAA3A3))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}