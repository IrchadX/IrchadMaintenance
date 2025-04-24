package com.example.irchadmaintenance.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    object DeviceList : Destination("device_list")
    object DeviceDetails : Destination("device_details/{deviceId}") {
        fun createRoute(deviceId: String): String {
            return this.route.replace("{deviceId}", deviceId)
        }

        val arguments = listOf(
            navArgument("deviceId") { type = NavType.StringType }
        )
    }
    object Interventions : Destination("add_interventions/{userId}") {
        fun createRoute(userId: String) : String {
            return this.route.replace("{userId}", userId)
        }

        val arguments = listOf(
            navArgument("userId") { type = NavType.StringType}
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
}