package com.example.irchadmaintenance.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    object DeviceList : Destination("device_list")

    object DeviceDetails : Destination("device_details/{userId}/{deviceId}") {
        fun createRoute(userId : String, deviceId: String): String {
            return this.route
                .replace("{userId}", userId)
                .replace("{deviceId}", deviceId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("deviceId") { type = NavType.StringType }
        )
    }
    object Interventions : Destination("add_interventions/{userId}/{deviceId}") {
        fun createRoute(userId: String, deviceId: String) : String {
            return this.route
                .replace("{userId}", userId)
                .replace("{deviceId}", deviceId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType},
            navArgument("deviceId") { type = NavType.StringType }
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
        fun createRoute(userId: String) : String {
            return this.route.replace("{userId}", userId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    }

    object NotificationDetails : Destination("notification_details/{userId}/{notificationId}/{deviceId}") {
        fun createRoute(userId : String, notificationId: String, deviceId : String) : String {
            return this.route
                .replace("{userId}", userId)
                .replace("{notificationId}", notificationId)
                .replace("{deviceId}", deviceId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("notificationId") { type = NavType.StringType }
        )
    }
}