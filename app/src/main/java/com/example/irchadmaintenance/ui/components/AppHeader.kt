package com.example.irchadmaintenance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.R
import com.example.irchadmaintenance.data.models.User
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.viewmodels.AuthViewModel

@Composable
fun AppHeader(user: User?, navController: NavController, title: String, default: Boolean, warning: Boolean = false,
              authViewModel: AuthViewModel
) {val user by authViewModel.user.collectAsState()
    Box() {
        Icon(
            painter = painterResource(id = R.drawable.vector_38),
            contentDescription = "Header Background Pic",
            tint = Color.Unspecified
        )
        Column {
            Column(
                modifier = Modifier
                    .padding(start = 24.dp, top = 29.dp, end = 24.dp)
            ) {
                AppHeaderCore(user, navController, title, default, warning)
            }
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4FE0DC),
                                Color(0xFF2B7A78),
                                Color(0xFF4FE0DC)
                            )
                        )
                    )
            )
        }
    }
}
@Composable
fun AppHeaderCore(user: User?, navController: NavController, title: String, default: Boolean, warning: Boolean) {
    var showBackConfirmationDialog by remember { mutableStateOf(false) }

    Column() {
        if (default) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF2B7A78))
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 23.dp, bottom = 26.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.irchad_logo),
                    contentDescription = "IRCHAD logo",
                    modifier = Modifier
                        .width(103.dp)
                        .height(27.dp),
                    tint = Color.Unspecified
                )
            }
        } else {
            CustomHeader(title, warning = warning, onBackClick = {
                if (warning) {
                    showBackConfirmationDialog = true
                } else {
                    navController.popBackStack()
                }
            })
        }

        Spacer(modifier = Modifier.height(29.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current

            // Fixed code for profile image
            if (user?.profilePicUrl != null) {
                val imageResId = remember(user.profilePicUrl) {
                    context.resources.getIdentifier(user.profilePicUrl, "drawable", context.packageName)
                }

                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "User profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {

                                navController.navigate(Destination.UserProfile.createRoute(user.id.toString()))
                            }
                    )
                } else {
                    // Fallback if resource not found but URL exists
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "User profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {

                                user?.id?.let { userId ->
                                    navController.navigate(Destination.UserProfile.createRoute(userId.toString()))
                                }  }
                    )
                }
            } else {
                // Default image when no profile URL exists
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "User profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            user?.id?.let { userId ->
                                navController.navigate(Destination.UserProfile.createRoute(userId.toString()))
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Bienvenue",
                    color = Color(0xFF2B7A78),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${user?.firstName ?: ""} ${user?.familyName ?: ""}".trim()
                        .ifEmpty { "User" },
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_icon),
                    contentDescription = "Notifications icon",
                    tint = Color(0xFF2B7A78),
                    modifier = Modifier
                        .clickable() {
                            user?.id?.let { userId ->
                                navController.navigate(Destination.Notifications.createRoute(userId.toString()))
                            }
                        }
                )
                user?.notificationCount?.let {
                    if (it > 0) {
                        Box(
                            modifier = Modifier
                                .offset(x = 4.dp, y = 6.dp)
                                .wrapContentSize()
                                .background(Color(0xFFFF0101), shape = CircleShape)
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (it > 99) "99+" else it.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.defaultMinSize(minWidth = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showBackConfirmationDialog) {
        BackConfirmationDialog(
            onConfirm = { navController.popBackStack() },
            onDismiss = { showBackConfirmationDialog = false }
        )
    }
}
@Composable
fun CustomHeader(title: String, warning: Boolean = false, onBackClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A8C84)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}