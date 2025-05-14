package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.R
import com.example.irchadmaintenance.data.User
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.BackConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userId: String,
    navController: NavController
) {

    val tealColor = Color(0xFF3AAFA9)
    val lightGreenBg = Color(0xFFEFF8F5)
    val redColor = Color(0xFFEF5A56)
    val grayTextColor = Color(0xFF9E9E9E)
    val graydiv = Color(0xFFE8E8E8)


    var showQrDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()


    val user = UserSampleData.users.find { it.userId == userId }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppHeader(
            user = user,
            navController = navController,
            title = "Profil",
            default = false,
            warning = false
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

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
                        modifier = Modifier.size(70.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "${user?.firstName ?: "N/A"} ${user?.familyName ?: "N/A"}",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = user?.phoneNumber ?: "N/A",
                        fontSize = 16.sp,
                        color = grayTextColor
                    )
                }

                TextButton(
                    onClick = {
                        showLogoutDialog = true
                    },
                ) {
                    Text(
                        text = "Déconnexion",
                        color = redColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = graydiv,
                thickness = 1.dp
            )
        }

        MenuItemRow(
            icon = Icons.Default.Person,
            title = "Mon Compte",
            onClick = {
                navController.navigate(Destination.AccountDetails.createRoute(userId))
            },
            tealColor = tealColor,
            lightGreenBg = lightGreenBg
        )

        Spacer(modifier = Modifier.height(16.dp))

        MenuItemRow(
            icon = Icons.Default.Info,
            title = "Mon Code d'Appairage",
            onClick = {
                showQrDialog = true
            },
            tealColor = tealColor,
            lightGreenBg = lightGreenBg
        )
    }

    if (showQrDialog) {
        ModalBottomSheet(
            onDismissRequest = { showQrDialog = false },
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
                    Text(
                        text = "Code d'Appairage",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Les utilisateurs d'Irchad peuvent utiliser ce code pour se connecter avec vous en tant qu'assistant.",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(lightGreenBg)
                        .padding(horizontal = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user?.identifier ?: "Pas d'identifiant",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    onClick = { showQrDialog = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(
                        text = "Annuler",
                        color = tealColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        ModalBottomSheet(
            onDismissRequest = { showLogoutDialog = false },
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
                    Text(
                        text = "Déconnexion",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Êtes-vous sûr de vouloir vous déconnecter? Vous devrez vous reconnecter pour accéder à votre compte.",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {
                            // Handle logout
                            showLogoutDialog = false
                            // Navigate to login screen
                            navController.navigate("login") { popUpTo(0) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tealColor
                        )
                    ) {
                        Text(
                            text = "Confirmer",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(
                        onClick = { showLogoutDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        Text(
                            text = "Annuler",
                            color = tealColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(
    icon: ImageVector,
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
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(lightGreenBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tealColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}
