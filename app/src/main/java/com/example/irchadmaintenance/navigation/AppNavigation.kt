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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.irchadmaintenance.repository.DeviceRepository
import com.example.irchadmaintenance.repository.InterventionRepository
import com.example.irchadmaintenance.state.AuthUIState
import com.example.irchadmaintenance.ui.screens.DeviceDetailsScreen
import com.example.irchadmaintenance.ui.screens.DevicesScreen
import com.example.irchadmaintenance.ui.screens.InterventionScreen
import com.example.irchadmaintenance.ui.screens.NotificationDetailsScreen
import com.example.irchadmaintenance.ui.screens.NotificationsScreen
import com.example.irchadmaintenance.ui.screens.SignInScreen
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
            else -> {} // Handle other states as needed
        }
    }

    val startDestination = Destination.Loading.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Loading screen as the initial destination
        composable(Destination.Loading.route) {
            LoadingScreen()

            // This LaunchedEffect ensures we react to the auth state from the loading screen
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
                    else -> {} // Keep showing loading screen for AuthUIState.Loading
                }
            }
        }

        // Authentication screens
        composable(Destination.SignIn.route) {
            SignInScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(Destination.DeviceList.route) {
            val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""

            // Only load devices if we have a valid userId
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
                currentRoute = currentRoute
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

        // Rest of your navigation definitions...
        composable(
            route = Destination.DeviceDetails.route,
            arguments = Destination.DeviceDetails.arguments
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute
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
            route = Destination.Interventions.route,
            arguments = Destination.Interventions.arguments
        ) { backStackEntry ->
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute
            ) {
                val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
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
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute
            ) {
                val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
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
            ContentScreenWithNavBar(
                navController = navController,
                currentRoute = currentRoute
            ) {
                val userId = (authState as? AuthUIState.Authenticated)?.userId ?: ""
                val notificationId = backStackEntry.arguments?.getInt("notificationId") ?: 0
                val deviceId = backStackEntry.arguments?.getInt("deviceId") ?: 0
                NotificationDetailsScreen(
                    userId = userId,
                    notificationId = notificationId,
                    deviceId = deviceId,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun ContentScreenWithNavBar(
    navController: NavHostController,
    currentRoute: String?,
    content: @Composable () -> Unit
) {
    Scaffold(

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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