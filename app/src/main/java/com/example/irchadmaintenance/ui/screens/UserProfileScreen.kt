package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userId: String,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()
    val userDevices = SampleData.devices.filter { it.userId == userId }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            user?.let { user ->
                Text(
                    text = "User ID: ${user.id}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Name: ${user.firstName ?: ""} ${user.familyName ?: ""}".trim()
                        .ifEmpty { "Unknown" },
                    fontSize = 16.sp
                )

                Text(
                    text = "Notifications: ${user.notificationCount}",
                    fontSize = 16.sp
                )

                if (userDevices.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Associated Devices (${userDevices.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    userDevices.forEach { device ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = device.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "ID: ${device.id}",
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Status: ${device.status}",
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Location: ${device.location}",
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Text(
                        text = "No devices associated with this user",
                        fontSize = 16.sp
                    )
                }
            } ?: Text("User not found with ID: $userId")
        }
    }
}