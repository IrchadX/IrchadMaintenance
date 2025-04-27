package com.example.irchadmaintenance.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.DeviceInfoList
import com.example.irchadmaintenance.ui.components.DiagnosticInfo

@Composable
fun DeviceDetailsScreen(deviceId: String, navController: NavController) {
    val device = SampleData.devices.find { it.id == deviceId }
    val user = UserSampleData.users.find { it.userId == device?.userId }
    var showDiagnostics by remember { mutableStateOf(false) }


    val rotationAngle by animateFloatAsState(
        targetValue = if (showDiagnostics) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrowRotation"
    )

    Column {
        AppHeader(
            user = user,
            navController = navController,
            title = "Dispositifs",
            false,
            false
        )

        if (device != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp)
                ) {
                    val context = LocalContext.current
                    device.imageUrl?.let { drawableName ->
                        val imageResId = remember(drawableName) {
                            context.resources.getIdentifier(drawableName, "drawable", context.packageName)
                        }

                        if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = device.name,
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
                        text = device.name,
                        fontSize = 22.3.sp,
                        color = Color(0xFF17252A),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(54.dp))

                DeviceInfoList(deviceId)

                Spacer(modifier = Modifier.height(105.dp))


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
                    DiagnosticInfo(
                        onRefresh = {
                            showDiagnostics = false
                            Handler(Looper.getMainLooper()).postDelayed({
                                showDiagnostics = true
                            }, 100)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            Destination.Interventions.createRoute(device.userId)
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
        }
    }
}