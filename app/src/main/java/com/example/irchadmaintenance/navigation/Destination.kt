package com.example.irchadmaintenance.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Navigation destinations for the app
 */
sealed class Destination(val route: String) {
    // Authentication routes
    object SignIn : Destination("signin")
    object SignUp : Destination("signup")
    object Loading : Destination("loading")
    object Account : Destination("account")

    // Main app routes
    object DeviceList : Destination("device_list")

    object DeviceDetails : Destination("device_details/{deviceId}") {
        fun createRoute(deviceId: String): String {
            return this.route.replace("{deviceId}", deviceId)
        }

        val arguments = listOf(
            navArgument("deviceId") { type = NavType.StringType }
        )
    }

    // Make sure this matches the navigation call in DeviceDetailsScreen.kt
    object Interventions : Destination("add_interventions/{userId}") {
        fun createRoute(userId: String) : String {
            return this.route.replace("{userId}", userId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    }

    object UserProfile : Destination("user_profile/{userId}") {
        fun createRoute(userId: String) : String {
            return this.route.replace("{userId}", userId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    }

    object Notifications : Destination("notifications/{userId}") {
        fun createRoute(userId: String): String {
            return this.route.replace("{userId}", userId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    }

    object NotificationDetails : Destination("notification_details/{userId}/{notificationId}/{deviceId}") {
        fun createRoute(userId: String, notificationId: Int, deviceId: Int?): String {
            return this.route
                .replace("{userId}", userId)
                .replace("{notificationId}", notificationId.toString())
                .replace("{deviceId}", (deviceId ?: 0).toString())
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("notificationId") { type = NavType.IntType },
            navArgument("deviceId") { type = NavType.IntType }
        )
    }
}