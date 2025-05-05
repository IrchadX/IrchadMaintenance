package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.DeviceList
import com.example.irchadmaintenance.ui.components.StatusFilterButton

@Composable
fun DevicesScreen(
    userId: String,
    devices: List<Device>,
    onDeviceClick: (String) -> Unit,
    navController: NavController
) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val filteredDevices = devices.filter { device ->

        val matchesSearch = device.name.contains(searchQuery, ignoreCase = true) ||
                device.id.toString().contains(searchQuery, ignoreCase = true) ||
                device.status.contains(searchQuery, ignoreCase = true)

        val matchesStatus = selectedStatus == null || device.status == selectedStatus

        matchesSearch && matchesStatus
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val user = UserSampleData.users.find { it.userId == userId }
        AppHeader(
            user,
            navController,
            "",
            true,
            false
        )

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Recherche...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusFilterButton(
                selectedStatus = selectedStatus,
                onStatusSelected = { status ->
                    selectedStatus = status
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        DeviceList(devices = filteredDevices, onDeviceClick = { deviceId ->
            onDeviceClick(deviceId)
        })
    }
}