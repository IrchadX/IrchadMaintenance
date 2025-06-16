package com.example.irchadmaintenance

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.irchadmaintenance.navigation.AppNavigation
import com.example.irchadmaintenance.notifications.NotificationHelper
import com.example.irchadmaintenance.repository.AuthRepository
import com.example.irchadmaintenance.repository.UserRepository
import com.example.irchadmaintenance.ui.theme.IRCHADMaintenanceTheme
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import com.example.irchadmaintenance.viewmodels.UserViewModel
import com.example.irchadmaintenance.websocket.WebSocketManager

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("MainActivity", "Notification permission granted")
            } else {
                Log.d("MainActivity", "Notification permission denied")
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupWebSocket() {
        val raspberryPiIp = "192.168.43.121"
        WebSocketManager.initialize(applicationContext, raspberryPiIp)

        WebSocketManager.connect { notification ->
            runOnUiThread {
                NotificationHelper.showNotification(
                    this,
                    notification.title,
                    notification.message
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthViewModel.initialize(applicationContext)
        NotificationHelper.createNotificationChannel(this)
        askNotificationPermission()
        setupWebSocket()
        enableEdgeToEdge()

        setContent {
            IRCHADMaintenanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel = AuthViewModel(AuthRepository(applicationContext))
                    val userViewModel = UserViewModel(UserRepository(applicationContext))

                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.disconnect()
    }
}