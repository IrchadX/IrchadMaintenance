package com.example.irchadmaintenance.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.irchadmaintenance.navigation.Destination

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun NavBar(
    navController: NavController,
    currentRoute: String?
) {
    val authenticatedUserId = "3" // Replace with actual user ID from AuthViewModel

    val items = listOf(
        BottomNavItem(
            name = "Devices",
            route = Destination.DeviceList.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = "Notifications",
            route = Destination.Notifications.createRoute(authenticatedUserId),
            icon = Icons.Default.Notifications
        ),
        BottomNavItem(
            name = "Profile",
            route = Destination.UserProfile.createRoute(authenticatedUserId),
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            name = "Account",
            route = Destination.Account.route,
            icon = Icons.Default.Settings
        )
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF3F51B5)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.name) },
                label = { Text(item.name) },
                selected = currentRoute == item.route,
                onClick = {
                    // Avoid re-navigating to the same route
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(Destination.DeviceList.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}