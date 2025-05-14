
package com.example.irchadmaintenance.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.ui.components.DeviceList
import com.example.irchadmaintenance.ui.screens.AccountDetailsScreen
import com.example.irchadmaintenance.ui.screens.DeviceDetailsScreen
import com.example.irchadmaintenance.ui.screens.DevicesScreen
import com.example.irchadmaintenance.ui.screens.InterventionDetailsScreen
import com.example.irchadmaintenance.ui.screens.InterventionScreen
import com.example.irchadmaintenance.ui.screens.InterventionsListScreen
import com.example.irchadmaintenance.ui.screens.LoginScreen
import com.example.irchadmaintenance.ui.screens.NotificationDetailsScreen
import com.example.irchadmaintenance.ui.screens.NotificationsScreen
import com.example.irchadmaintenance.ui.screens.UserProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.Login.route
    ) {

        composable(Destination.Login.route) {
            LoginScreen(navController = navController)
        }


        composable(
            Destination.DeviceList.route
        ) {
            val userId = "user001"
            DevicesScreen(
                userId,
                devices = SampleData.devices,
                onDeviceClick = { deviceId ->
                    navController.navigate(
                        Destination.DeviceDetails.createRoute(userId, deviceId)
                    )
                },
                navController = navController
            )
        }

        composable(
            route = Destination.DeviceDetails.route,
            arguments = Destination.DeviceDetails.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            DeviceDetailsScreen(userId = userId, deviceId = deviceId, navController = navController)
        }

        composable(
            route = Destination.Interventions.route,
            arguments = Destination.Interventions.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            InterventionScreen(userId = userId,deviceId = deviceId, navController = navController)
        }

        composable(
            route = Destination.InterventionsList.route,
            arguments = Destination.InterventionsList.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            InterventionsListScreen(userId = userId, navController = navController)
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

        composable(
            route = Destination.NotificationDetails.route,
            arguments = Destination.NotificationDetails.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val notificationId = backStackEntry.arguments?.getString("notificationId") ?: ""
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            NotificationDetailsScreen(userId = userId, notificationId = notificationId, deviceId = deviceId, navController = navController)
        }

        composable(
            route = Destination.InterventionDetails.route,
            arguments = Destination.InterventionDetails.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val interventionId = backStackEntry.arguments?.getString("interventionId") ?: ""
            InterventionDetailsScreen(
                userId = userId,
                interventionId = interventionId,
                navController = navController
            )
        }

        composable(
            route = Destination.AccountDetails.route,
            arguments = Destination.AccountDetails.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AccountDetailsScreen(userId = userId, navController = navController)
        }

    }
}