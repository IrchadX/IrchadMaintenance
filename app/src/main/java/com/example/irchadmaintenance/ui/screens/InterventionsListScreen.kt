package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.InterventionSampleData
import com.example.irchadmaintenance.data.InterventionStatus
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.InterventionCard

@Composable
fun InterventionsListScreen(
    userId: String,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<InterventionStatus?>(null) }

    val interventions = InterventionSampleData.interventions.filter { it.userId == userId }

    val filteredInterventions = interventions.filter { intervention ->
        val matchesSearch = intervention.title.contains(searchQuery, ignoreCase = true) ||
                intervention.description.contains(searchQuery, ignoreCase = true) ||
                intervention.date.contains(searchQuery, ignoreCase = true)
        val matchesStatus = selectedStatus == null || intervention.status == selectedStatus

        matchesSearch && matchesStatus
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val user = UserSampleData.users.find { it.userId == userId }
        AppHeader(
            user = user,
            navController = navController,
            title = "Interventions",
            default = false,
            warning = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Rechercher une intervention") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { selectedStatus = null },
                label = { Text("Tous") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2B7A78),
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            )

            FilterChip(
                selected = selectedStatus == InterventionStatus.EN_MAINTENANCE,
                onClick = { selectedStatus = InterventionStatus.EN_MAINTENANCE },
                label = { Text("En maintenance") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2B7A78),
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            )

            FilterChip(
                selected = selectedStatus == InterventionStatus.DONE,
                onClick = { selectedStatus = InterventionStatus.DONE },
                label = { Text("Terminé") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2B7A78),
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredInterventions) { intervention ->
                InterventionCard(
                    intervention = intervention,
                    onCardClick = { interventionId ->
                        navController.navigate(
                            "intervention_details/${userId}/${interventionId}"
                        )
                    }
                )
            }
        }
    }
}