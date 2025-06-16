package com.example.irchadmaintenance.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.irchadmaintenance.navigation.Destination
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.InterventionCard
import com.example.irchadmaintenance.viewmodels.InterventionViewModel
import com.example.irchadmaintenance.viewmodels.AuthViewModel

@Composable
fun InterventionsListScreen(
    userId: String,
    navController: NavController,
    viewModel: InterventionViewModel,
    authViewModel: AuthViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val interventions by remember { derivedStateOf { viewModel.interventions } }
    val isLoading by viewModel.isLoading
    val error by viewModel.error

    // Get user from AuthViewModel
    val user by authViewModel.user.collectAsState()

    // Load interventions when screen opens
    LaunchedEffect(selectedStatus) {
        when (selectedStatus) {
            "pending" -> viewModel.loadInterventionsByStatus("pending")
            "completed" -> viewModel.loadInterventionsByStatus("completed")
            else -> viewModel.loadInterventions()
        }
    }

    // Filter interventions by user ID and search query
    val filteredInterventions = interventions.filter { intervention ->
        val belongsToUser = intervention.maintenancerId == userId
        val matchesSearch = intervention.title?.contains(searchQuery, ignoreCase = true) == true ||
                intervention.description?.contains(searchQuery, ignoreCase = true) == true ||
                intervention.scheduledDate.contains(searchQuery, ignoreCase = true)

        belongsToUser && matchesSearch
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fixed AppHeader call with authViewModel parameter
        AppHeader(
            user = user, // Use user from AuthViewModel
            navController = navController,
            title = "Interventions",
            default = false,
            warning = false,
            authViewModel = authViewModel // Add the missing parameter
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

        // Status filter chips
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
                selected = selectedStatus == "pending",
                onClick = { selectedStatus = "pending" },
                label = { Text("En cours") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2B7A78),
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            )

            FilterChip(
                selected = selectedStatus == "completed",
                onClick = { selectedStatus = "completed" },
                label = { Text("Terminé") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2B7A78),
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading state
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF2B7A78))
            }
        }

        // Error state
        error?.let { errorMessage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = "Erreur: $errorMessage",
                    color = Color(0xFFD32F2F),
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Interventions list
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
                            Destination.InterventionDetails.createRoute(userId, interventionId)
                        )
                    }
                )
            }

            // Empty state
            if (filteredInterventions.isEmpty() && !isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotEmpty())
                                "Aucune intervention trouvée pour \"$searchQuery\""
                            else "Aucune intervention disponible",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}