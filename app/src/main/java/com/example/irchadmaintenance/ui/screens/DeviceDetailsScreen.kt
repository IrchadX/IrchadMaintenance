package com.example.irchadmaintenance.ui.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.DiagnosticInfo
import com.example.irchadmaintenance.viewmodels.DeviceViewModel
import com.example.irchadmaintenance.data.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.state.AuthUIState
import com.example.irchadmaintenance.ui.components.OSMDroidMap
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun DeviceDetailsScreen(
    deviceId: String,
    navController: NavController,
    viewModel: DeviceViewModel,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    var device by remember { mutableStateOf<Device?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showDiagnostics by remember { mutableStateOf(false) }
    var diagnosticData by remember { mutableStateOf<DeviceDiagnosticApiModel?>(null) }
    var isLoadingDiagnostic by remember { mutableStateOf(false) }
    val userLocation by viewModel.userLocation.collectAsState()

    // Get the current user ID for WebSocket connection
    val currentUserId = remember(authState) {
        (authState as? AuthUIState.Authenticated)?.userId ?: ""
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (showDiagnostics) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrowRotation"
    )

    LaunchedEffect(deviceId) {
        try {
            val numericId = deviceId.toIntOrNull()
            if (numericId != null) {
                device = viewModel.loadDeviceById(numericId)

                // Start location tracking with proper parameters
                if (currentUserId.isNotEmpty()) {
                    // Use device owner's user ID as publisher and current user as subscriber
                    val publisherId = device?.userId?.toString() ?: currentUserId
                    viewModel.startListeningForLocation(
                        userId = currentUserId,
                        publisherId = publisherId
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("DeviceDetailsScreen", "Failed to load device details", e)
        } finally {
            isLoading = false
        }
    }

    // Clean up WebSocket connection when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListening()
        }
    }

    suspend fun runDiagnostic() {
        isLoadingDiagnostic = true
        try {
            val numericId = deviceId.toIntOrNull()
            if (numericId != null) {
                diagnosticData = viewModel.runDiagnostic(numericId)
            }
        } catch (e: Exception) {
            Log.e("DeviceDetailsScreen", "Failed to run diagnostic", e)
        } finally {
            isLoadingDiagnostic = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            user = user,
            navController = navController,
            title = "",
            default = true,
            warning = false,
            authViewModel = authViewModel
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (device != null) {
            val currentDevice = device!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Device Image and Name Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp)
                ) {
                    val context = LocalContext.current
                    currentDevice.imageName?.let { drawableName ->
                        val imageResId = remember(drawableName) {
                            context.resources.getIdentifier(drawableName, "drawable", context.packageName)
                        }

                        if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = currentDevice.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(224.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentDevice.name,
                        fontSize = 22.3.sp,
                        color = Color(0xFF17252A),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(78.dp))

                // Real-time Location Map Section
                if (userLocation != null) {
                    OSMDroidMap(
                        location = "Localisation du client en temps réel",
                        latitude = userLocation!!.latitude,
                        longitude = userLocation!!.longitude
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(216.dp)
                            .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color(0xFF3AAFA9))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "En attente de la localisation du client...",
                                color = Color(0xFF17252A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Device Details Section
                DeviceDetailsInfo(currentDevice)

                Spacer(modifier = Modifier.height(48.dp))

                // Diagnostic Button and Section
                Button(
                    onClick = { showDiagnostics = !showDiagnostics },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2B7A78),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Effectuer un diagnostic",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 22.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,

                        contentDescription = "Expand",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .rotate(rotationAngle),
                        tint = Color.White
                    )
                }

                AnimatedVisibility(
                    visible = showDiagnostics,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    LaunchedEffect(showDiagnostics) {
                        if (showDiagnostics && diagnosticData == null) {
                            runDiagnostic()
                        }
                    }

                    if (isLoadingDiagnostic) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        DiagnosticInfo(
                            diagnosticData = diagnosticData,
                            onRefresh = {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    viewModel.viewModelScope.launch {
                                        runDiagnostic()
                                    }
                                }, 100)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Intervention Button
                Button(
                    onClick = {
                        val userId = currentUserId
                        navController.navigate(
                            Destination.Interventions.createRoute(
                                userId = userId,
                                deviceId = deviceId
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF17252A),
                        contentColor = Color(0xFFFCFFFE)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                ) {
                    Text(
                        text = "Intervenir",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Dispositif non trouvé", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun DeviceDetailsInfo(device: Device) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DeviceInfoRow("Type", device.type)
            DeviceInfoRow("Statut", device.status)
            DeviceInfoRow("Adresse MAC", device.macAddress ?: "Non disponible")
            DeviceInfoRow("Version du logiciel", device.softwareVersion ?: "Non disponible")
            DeviceInfoRow("Date d'activation", device.activationDate)
            DeviceInfoRow("Location", device.location)
        }
    }
}

@Composable
fun DeviceInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF17252A)
        )
        Text(
            text = value,
            color = Color(0xFF3AAFA9)
        )
    }
    Divider(
        modifier = Modifier.padding(vertical = 6.dp),
        color = Color(0xFFDEF2F1)
    )
}