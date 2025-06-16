package com.example.irchadmaintenance.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.irchadmaintenance.repository.DeviceRepository
import com.example.irchadmaintenance.repository.InterventionRepository
import com.example.irchadmaintenance.state.AuthUIState
import com.example.irchadmaintenance.ui.components.BottomNavigationBar
import com.example.irchadmaintenance.ui.screens.AccountScreen
import com.example.irchadmaintenance.ui.screens.DeviceDetailsScreen
import com.example.irchadmaintenance.ui.screens.DevicesScreen
import com.example.irchadmaintenance.ui.screens.InterventionDetailsScreen
import com.example.irchadmaintenance.ui.screens.InterventionScreen
import com.example.irchadmaintenance.ui.screens.InterventionsListScreen
import com.example.irchadmaintenance.ui.screens.NotificationDetailsScreen
import com.example.irchadmaintenance.ui.screens.NotificationsScreen
import com.example.irchadmaintenance.ui.screens.SignInScreen
import com.example.irchadmaintenance.ui.screens.UserProfileScreen
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import com.example.irchadmaintenance.viewmodels.DeviceViewModel
import com.example.irchadmaintenance.viewmodels.InterventionViewModel
import com.example.irchadmaintenance.viewmodels.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
) {
    val authState by authViewModel.authState.collectAsState()

    // Get current route for the NavBar
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val deviceRepository = DeviceRepository()
    val deviceViewModel = remember { DeviceViewModel(deviceRepository) }

    val interventionRepository = InterventionRepository()
    val interventionViewModel = remember { InterventionViewModel(interventionRepository) }

    // Log the current auth state for debugging
    LaunchedEffect(authState) {
        Log.d("AppNavigation", "Auth state changed to: $authState")

        // Force navigation based on auth state changes
        when (authState) {
            is AuthUIState.Authenticated -> {
                if (currentRoute != Destination.DeviceList.route) {
                    navController.navigate(Destination.DeviceList.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
            is AuthUIState.Unauthenticated -> {
                if (currentRoute != Destination.SignIn.route) {
                    navController.navigate(Destination.SignIn.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
            else -> {}
        }
    }

    val startDestination = Destination.Loading.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Destination.Loading.route) {
            LoadingScreen()

            LaunchedEffect(authState) {
                when (authState) {
                    is AuthUIState.Authenticated -> {
                        val userId = (authState as AuthUIState.Authenticated).userId
                        Log.d("Loading Screen", "User authenticated with ID: $userId")
                        navController.navigate(Destination.DeviceList.route) {
                            popUpTo(Destination.Loading.route) { inclusive = true }
                        }
                    }
                    is AuthUIState.Unauthenticated -> {
                        Log.d("Loading Screen", "User not authenticated, redirecting to sign in")
                        navController.navigate(Destination.SignIn.route) {
                            popUpTo(Destination.Loading.route) { inclusive = true }
                        }
                    }
                    else -> {}
                }
            }
        }

        composable(Destination.SignIn.route) {
            SignInScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(Destination.DeviceList.route) {
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""

            if (userId.isNotEmpty()) {
                LaunchedEffect(key1 = userId) {
                    try {
                        deviceViewModel.loadDevicesForUser(userId.toInt())
                    } catch (e: Exception) {
                        Log.e("AppNavigation", "Failed to load devices", e)
                    }
                }
            }

            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                DevicesScreen(
                    userId = userId,
                    devices = deviceViewModel.devices,
                    onDeviceClick = { deviceId ->
                        navController.navigate(
                            Destination.DeviceDetails.createRoute(deviceId)
                        )
                    },
                    navController = navController,
                    viewModel = deviceViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.DeviceDetails.route,
            arguments = Destination.DeviceDetails.arguments
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""

            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                DeviceDetailsScreen(
                    deviceId = deviceId,
                    navController = navController,
                    viewModel = deviceViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.InterventionsList.route,
            arguments = Destination.InterventionsList.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                InterventionsListScreen(
                    userId = userId,
                    navController = navController,
                    viewModel = interventionViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.Interventions.route,
            arguments = Destination.Interventions.arguments
        ) { backStackEntry ->
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                InterventionScreen(
                    userId = userId,
                    navController = navController,
                    viewModel = interventionViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.Notifications.route,
            arguments = Destination.Notifications.arguments
        ) { backStackEntry ->
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                NotificationsScreen(
                    userId = userId,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.NotificationDetails.route,
            arguments = Destination.NotificationDetails.arguments
        ) { backStackEntry ->
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
            val notificationId = backStackEntry.arguments?.getInt("notificationId") ?: 0
            val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: 0
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                NotificationDetailsScreen(
                    userId = userId,
                    notificationId = notificationId,
                    deviceId = deviceId,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        composable(
            route = Destination.UserProfile.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                UserProfileScreen(
                    userId = userId,
                    navController = navController,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    onSignOut = { authViewModel.signOut() }
                )
            }
        }

        // FIXED: Moved InterventionDetails composable inside NavHost
        composable(
            route = Destination.InterventionDetails.route,
            arguments = Destination.InterventionDetails.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val interventionId = backStackEntry.arguments?.getString("interventionId") ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                InterventionDetailsScreen(
                    userId = userId,
                    interventionId = interventionId,
                    navController = navController,
                    viewModel = interventionViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(Destination.Account.route) {
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute,
                userId = userId
            ) {
                AccountScreen(
                    navController = navController,
                    userviewModel = userViewModel,
                    authviewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun ContentScreenWithNavBar(
    navController: NavHostController,
    currentRoute: String?,
    userId: String,
    content: @Composable () -> Unit
) {
    // Define routes that should show the bottom navigation
    val routesWithBottomNav = listOf(
        Destination.DeviceList.route,
        "device_details/{deviceId}",
        "add_interventions/{userId}/{deviceId}",
        "interventions_list/{userId}",
        "intervention_details/{userId}/{interventionId}",
        "user_profile/{userId}",
        "notifications/{userId}",
        "notification_details/{userId}/{notificationId}/{deviceId}"
    )

    val shouldShowBottomNav = currentRoute?.let { route ->
        routesWithBottomNav.any { navRoute ->
            // Check if current route matches any of the nav routes (considering parameters)
            when {
                route.startsWith("device_list") -> true
                route.startsWith("device_details") -> true
                route.startsWith("add_interventions") -> true
                route.startsWith("interventions_list") -> true
                route.startsWith("intervention_details") -> true
                route.startsWith("user_profile") -> true
                route.startsWith("notifications") -> true
                route.startsWith("notification_details") -> true
                else -> false
            }
        }
    } ?: false

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav && userId.isNotEmpty()) {
                BottomNavigationBar(
                    navController = navController,
                    userId = userId
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // This automatically handles the navbar padding
        ) {
            content()
        }
    }
}

/**
 * Loading screen shown while determining authentication state
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF6B7CFF))
    }
}