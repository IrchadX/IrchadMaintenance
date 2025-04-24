package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.SampleData
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.ui.components.AppHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    userId: String,
    navController: NavController
) {
    val user = UserSampleData.users.find { it.userId == userId }

    val userDevices = SampleData.devices.filter { it.userId == userId }

   Column (
       modifier = Modifier
           .fillMaxSize()
   ){
       AppHeader(
           user = user,
           navController = navController,
           title = "Alertes",
           default = false,
           warning = false
       )
   }
}