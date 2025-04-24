
package com.example.irchadmaintenance.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.ui.components.DeviceList
import com.example.irchadmaintenance.ui.screens.DeviceDetailsScreen
import com.example.irchadmaintenance.ui.screens.DevicesScreen
import com.example.irchadmaintenance.ui.screens.InterventionScreen
import com.example.irchadmaintenance.ui.screens.NotificationsScreen
import com.example.irchadmaintenance.ui.screens.UserProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.DeviceList.route
    ) {

        composable(Destination.DeviceList.route) {
            DevicesScreen(
                userId = "user001",
                devices = SampleData.devices,
                onDeviceClick = { deviceId ->
                    navController.navigate(
                        Destination.DeviceDetails.createRoute(deviceId)
                    )
                },
                navController = navController
            )
        }

        composable(
            route = Destination.DeviceDetails.route,
            arguments = Destination.DeviceDetails.arguments
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            DeviceDetailsScreen(deviceId = deviceId, navController = navController)
        }

        composable(
            route = Destination.Interventions.route,
            arguments = Destination.Interventions.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            InterventionScreen(userId = userId, navController = navController)
        }

        composable(
            route = Destination.UserProfile.route,
            arguments = Destination.UserProfile.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreen (userId = userId, navController = navController)
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