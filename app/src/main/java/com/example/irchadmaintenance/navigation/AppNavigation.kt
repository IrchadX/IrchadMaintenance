package com.example.irchadmaintenance.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.irchadmaintenance.repository.DeviceRepository
import com.example.irchadmaintenance.repository.InterventionRepository
import com.example.irchadmaintenance.ui.screens.DeviceDetailsScreen
import com.example.irchadmaintenance.ui.screens.DevicesScreen
import com.example.irchadmaintenance.ui.screens.InterventionScreen
import com.example.irchadmaintenance.ui.screens.NotificationsScreen
import com.example.irchadmaintenance.ui.screens.UserProfileScreen
import com.example.irchadmaintenance.ui.viewmodels.DeviceViewModel
import com.example.irchadmaintenance.ui.viewmodels.InterventionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    val deviceRepository = DeviceRepository()
    val deviceViewModel = remember { DeviceViewModel(deviceRepository) }

    val interventionRepository = InterventionRepository()
    val interventionViewModel = remember { InterventionViewModel(interventionRepository) }

    LaunchedEffect(key1 = true) {
        try {
            deviceViewModel.loadDevicesForUser(3)
        } catch (e: Exception) {
            Log.e("AppNavigation", "Failed to load devices", e)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.DeviceList.route
    ) {
        composable(Destination.DeviceList.route) {
            DevicesScreen(
                userId = "74",
                devices = deviceViewModel.devices,
                onDeviceClick = { deviceId ->
                    navController.navigate(
                        Destination.DeviceDetails.createRoute(deviceId)
                    )
                },
                navController = navController,
                viewModel = deviceViewModel
            )
        }

        composable(
            route = Destination.DeviceDetails.route,
            arguments = Destination.DeviceDetails.arguments
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            DeviceDetailsScreen(
                deviceId = deviceId,
                navController = navController,
                viewModel = deviceViewModel
            )
        }

        composable(
            route = Destination.Interventions.route,
            arguments = Destination.Interventions.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            InterventionScreen(
                userId = userId,
                navController = navController,
                viewModel = interventionViewModel
            )
        }

        composable(
            route = Destination.UserProfile.route,
            arguments = Destination.UserProfile.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreen(userId = userId, navController = navController)
        }

        composable(
            route = Destination.Notifications.route,
            arguments = Destination.Notifications.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            NotificationsScreen(userId = userId, navController = navController)
        }
    }
}