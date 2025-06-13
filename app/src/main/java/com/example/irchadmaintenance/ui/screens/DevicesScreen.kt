package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.DeviceList
import com.example.irchadmaintenance.ui.components.StatusFilterButton
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import com.example.irchadmaintenance.viewmodels.DeviceViewModel

import com.example.irchadmaintenance.data.models.User

@Composable
fun DevicesScreen(
    userId: String,
    devices: List<Device>,
    onDeviceClick: (String) -> Unit,
    navController: NavController,
    viewModel: DeviceViewModel,
    authViewModel: AuthViewModel
) {val user by authViewModel.user.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    // Get the loading and error states
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value

    val filteredDevices = devices.filter { device ->
        val matchesSearch = device.name.contains(searchQuery, ignoreCase = true) ||
                device.id.contains(searchQuery, ignoreCase = true) ||
                device.status.contains(searchQuery, ignoreCase = true) ||
                device.location.contains(searchQuery, ignoreCase = true)

        val matchesStatus = selectedStatus == null || device.status == selectedStatus

        matchesSearch && matchesStatus
    }

    Column(modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            user = user, // Use the authenticated user
            navController = navController,
            title = "",
            default = true,
            warning = false,
            authViewModel = authViewModel
        )


        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadDevicesForUser(userId.toIntOrNull() ?: 1) }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            // Rest of your UI
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

            // Add this to show when there are no devices
            if (filteredDevices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No devices found")
                }
            } else {
                DeviceList(devices = filteredDevices, onDeviceClick = onDeviceClick)
            }
        }
    }
}