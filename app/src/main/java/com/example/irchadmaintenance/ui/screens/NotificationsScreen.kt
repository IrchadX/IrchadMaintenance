package com.example.irchadmaintenance.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.NotificationsList
import com.example.irchadmaintenance.viewmodels.NotificationsViewModel

@Composable
fun NotificationsScreen(
    userId: String,
 //   alerts: List<Notification>,
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel()
) {
    val context = LocalContext.current
    val viewModel: NotificationsViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.listenForRealTimeAlerts(context)
    }
    val user = UserSampleData.users.find { it.userId == userId }

    val notifications = viewModel.notifications

    var selectedTab by remember { mutableStateOf(0) }
    val unHandledCount = notifications.count { !it.isHandled }



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            user = user,
            navController = navController,
            title = "Alertes",
            default = false,
            warning = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedTab = 0 }
            ) {
                Text(
                    text = "Toutes ${notifications.size}",
                    fontSize = 14.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedTab == 0) Color(0xFF2B7A78) else Color(0xFF64748B),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            if (selectedTab == 0) Color(0xFF2B7A78) else Color.Transparent
                        )
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedTab = 1 }
            ) {
                Text(
                    text = "Non gérées $unHandledCount",
                    fontSize = 14.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedTab == 1) Color(0xFF2B7A78) else Color(0xFF64748B),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            if (selectedTab == 1) Color(0xFF2B7A78) else Color.Transparent
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        NotificationsList(
            notifications = notifications,
            showOnlyUnread = selectedTab == 1,
            onNotificationClick = { notification ->
                navController.navigate(
                    Destination.NotificationDetails.createRoute(userId, notification.id, notification.deviceId)
                )
            }
        )
    }
}