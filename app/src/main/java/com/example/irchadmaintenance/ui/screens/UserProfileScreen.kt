package com.example.irchadmaintenance.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import com.example.irchadmaintenance.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.material.Divider
import androidx.compose.material.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.state.AuthUIState
import com.example.irchadmaintenance.ui.theme.Maingreen
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import com.example.irchadmaintenance.viewmodels.UserViewModel
import kotlinx.coroutines.flow.firstOrNull
import androidx.compose.material3.OutlinedTextField
import com.example.irchadmaintenance.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userId: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onSignOut: () -> Unit = {}
) {
    val tealColor = Color(0xFF3E7063)
    val lightGreenBg = Color(0xFFEFF8F5)
    val redColor = Color(0xFFEF5A56)
    val grayTextColor = Color(0xFF9E9E9E)
    val graydiv = Color(0xFFE8E8E8)
    val showLogoutDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val authState by authViewModel.authState.collectAsState()
    val userDevices = SampleData.devices.filter { it.userId == userId }


    val user by userViewModel.user.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthUIState.Authenticated) {
            val userId = (authState as AuthUIState.Authenticated).userId
            val token = authViewModel.authRepository.getAuthToken().firstOrNull()
            if (!token.isNullOrBlank()) {
                Log.d("ProfileScreen", "Fetching user details for ID: $userId")
                userViewModel.fetchUserDetails(userId)
            }
        }
    }

// Log the fetched user once it's available
    LaunchedEffect(user) {
        if (user != null) {
            Log.d("ProfileScreen", "User details fetched: $user")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                //.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            user?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                       // .padding(top = 24.dp)
                       // .background(Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                    ) {
                        // Profile section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Profile image
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(lightGreenBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user),
                                    contentDescription = "Profile Avatar",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            // Name and phone
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp)
                            ) {
                                androidx.compose.material.Text(
                                    text = "${user?.firstName ?: "N/A"} ${user?.familyName ?: "N/A"}",
                                    color = Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                androidx.compose.material.Text(
                                    text = user?.phoneNumber ?: "N/A",
                                    fontSize = 16.sp,
                                    color = grayTextColor
                                )
                            }

                            TextButton(
                                onClick = {
                                    showLogoutDialog.value = true
                                },
                            ) {
                                androidx.compose.material.Text(
                                    text = "Logout",
                                    color = redColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = graydiv,
                            thickness = 0.9.dp
                        )
                    }

                    // Menu items
                    MenuItemRow(
                        icon = Icons.Default.Person,
                        title = "My Account",
                        onClick = {
                            navController.navigate(Destination.Account.route)
                        },
                        tealColor = tealColor,
                        lightGreenBg = lightGreenBg
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                }


                if (showLogoutDialog.value) {
                    ModalBottomSheet(
                        onDismissRequest = { showLogoutDialog.value = false },
                        sheetState = sheetState,
                        containerColor = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 520.dp)
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {

                                androidx.compose.material.Text(
                                    text = "Logout",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                androidx.compose.material.Text(
                                    text = "Are you sure you want to log out? You'll need to sign in again to access your account.",
                                    fontSize = 16.sp,
                                    color = Color.DarkGray,
                                    textAlign = TextAlign.Start
                                )

                                Spacer(modifier = Modifier.height(24.dp))


                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            )
                            {
                                Button(
                                    onClick = onSignOut,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Maingreen,
                                        contentColor = Color.White
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 8.dp
                                    )
                                ){
                                    androidx.compose.material.Text(
                                        text = "Confirm",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                TextButton(
                                    onClick = { showLogoutDialog.value = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.dp)
                                ) {
                                    androidx.compose.material.Text(
                                        text = "Cancel",
                                        color = tealColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }


                        }
                    }
                }






            } ?: Text("User not found with ID: $userId")
        }
    }
}

@Composable
fun MenuItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    tealColor: Color,
    lightGreenBg: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(lightGreenBg),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material.Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tealColor,
                modifier = Modifier.size(24.dp)
            )
        }

        // Title
        androidx.compose.material.Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        // Chevron icon
        androidx.compose.material.Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}