package com.example.irchadmaintenance.ui.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.irchadmaintenance.navigation.Destination

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

object BottomNavItems {
    val Home = BottomNavItem(
        route = Destination.DeviceList.route,
        title = "Accueil",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    val Interventions = BottomNavItem(
        route = Destination.InterventionsList.createRoute("user001"), // placeholder
        title = "Interventions",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    )

    val Profile = BottomNavItem(
        route = "profile_route",
        title = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    userId: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val items = listOf(
        BottomNavItems.Home,
        BottomNavItems.Interventions.copy(
            route = Destination.InterventionsList.createRoute(userId)
        ),
        BottomNavItems.Profile.copy(
            route = Destination.UserProfile.createRoute(userId)
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), // Only round top corners
                ambientColor = Color(0x0F20845A),
                spotColor = Color(0x0F20845A)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) // Only round top corners
            .background(Color(0xFFEFF7F6))
            .padding(horizontal = 16.dp, vertical = 12.dp), // Adjust padding for better spacing
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val selected = when (item.title) {
                "Accueil" -> currentRoute.startsWith("device_list") ||
                        currentRoute.startsWith("device_details") ||
                        currentRoute.startsWith("add_interventions")

                "Interventions" -> currentRoute.startsWith("interventions_list") ||
                        currentRoute.startsWith("intervention_details")

                "Profil" -> currentRoute.startsWith("user_profile")

                else -> false
            }

            val backgroundColor by animateColorAsState(
                if (selected) Color(0xFF2B7A78) else Color.Transparent,
                label = "TabBackgroundColor"
            )

            val contentColor by animateColorAsState(
                if (selected) Color.White else Color(0xFF2B7A78),
                label = "TabContentColor"
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .clickable {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(horizontal = if (selected) 16.dp else 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.title,
                    tint = contentColor
                )
                AnimatedVisibility(visible = selected) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = item.title, color = contentColor)
                }
            }
        }
    }

}