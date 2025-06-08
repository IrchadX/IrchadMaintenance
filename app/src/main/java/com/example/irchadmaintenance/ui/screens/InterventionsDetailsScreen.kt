package com.example.irchadmaintenance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.models.Intervention
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.BackConfirmationDialog
import com.example.irchadmaintenance.ui.components.FormTextField
import com.example.irchadmaintenance.ui.components.InterventionStatusChip
import com.example.irchadmaintenance.ui.components.SuccessToast
import com.example.irchadmaintenance.viewmodels.AuthViewModel
import com.example.irchadmaintenance.viewmodels.InterventionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InterventionDetailsScreen(
    userId: String,
    interventionId: String,
    navController: NavController,
    viewModel: InterventionViewModel,
    authViewModel: AuthViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val user by authViewModel.user.collectAsState()

    // State for the intervention
    var intervention by remember { mutableStateOf<Intervention?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Form states
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSuccessToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    // Load intervention details
    LaunchedEffect(interventionId) {
        try {
            isLoading = true
            error = null
            val loadedIntervention = viewModel.getInterventionById(interventionId.toInt())
            intervention = loadedIntervention
            title = loadedIntervention.title ?: ""
            description = loadedIntervention.description ?: ""
        } catch (e: Exception) {
            error = e.message ?: "Failed to load intervention"
        } finally {
            isLoading = false
        }
    }

    val isInMaintenance = intervention?.status?.lowercase() == "pending" ||
            intervention?.status?.lowercase() == "in_progress"

    fun showToastAndNavigateBack(message: String) {
        toastMessage = message
        showSuccessToast = true
        coroutineScope.launch {
            delay(1500)
            navController.popBackStack()
        }
    }

    fun saveChanges() {
        coroutineScope.launch {
            try {
                intervention?.let { currentIntervention ->
                    viewModel.updateIntervention(
                        id = currentIntervention.id.toInt(),
                        title = title,
                        description = description
                    )
                    // Update local state
                    intervention = currentIntervention.copy(
                        title = title,
                        description = description
                    )
                    isEditing = false
                    toastMessage = "Modifications enregistrées"
                    showSuccessToast = true
                }
            } catch (e: Exception) {
                // Handle error - you might want to show an error toast
                error = e.message ?: "Failed to save changes"
            }
        }
    }

    fun deleteIntervention() {
        coroutineScope.launch {
            try {
                intervention?.let { currentIntervention ->
                    val success = viewModel.deleteIntervention(currentIntervention.id.toInt())
                    if (success) {
                        showToastAndNavigateBack("Intervention supprimée")
                    } else {
                        error = "Échec de la suppression de l'intervention"
                    }
                }
            } catch (e: Exception) {
                error = e.message ?: "Failed to delete intervention"
            }
        }
    }

    fun completeIntervention() {
        coroutineScope.launch {
            try {
                intervention?.let { currentIntervention ->
                    viewModel.completeIntervention(currentIntervention.id.toInt())
                    intervention = currentIntervention.copy(status = "completed")
                    showToastAndNavigateBack("Intervention terminée")
                }
            } catch (e: Exception) {
                error = e.message ?: "Failed to complete intervention"
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            user = user,
            navController = navController,
            title = "Détails Intervention",
            default = false,
            warning = false,
            authViewModel = authViewModel
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2B7A78))
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Erreur: $error",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2B7A78)
                            )
                        ) {
                            Text("Retour")
                        }
                    }
                }
            }

            intervention != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        InterventionStatusChip(status = intervention!!.status)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isEditing) {
                        FormTextField(
                            label = "Titre",
                            value = title,
                            onValueChange = { title = it }
                        )
                    } else {
                        Text(
                            text = "Titre",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3AAFA9)
                        )

                        Text(
                            text = title.ifEmpty { "Pas de titre" },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Date",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3AAFA9)
                    )

                    Text(
                        text = intervention!!.scheduledDate,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Localisation",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3AAFA9)
                    )

                    Text(
                        text = intervention!!.location.ifEmpty { "Non spécifiée" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Type",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3AAFA9)
                    )

                    Text(
                        text = if (intervention!!.type == "technique") "Préventive" else "Curative",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isEditing) {
                        FormTextField(
                            label = "Description",
                            value = description,
                            onValueChange = { description = it },
                            singleLine = false,
                            modifier = Modifier.height(150.dp)
                        )
                    } else {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3AAFA9)
                        )

                        Text(
                            text = description.ifEmpty { "Pas de description" },
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isInMaintenance) {
                        if (isEditing) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        title = intervention!!.title ?: ""
                                        description = intervention!!.description ?: ""
                                        isEditing = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Gray
                                    ),
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                ) {
                                    Text("Annuler")
                                }

                                Button(
                                    onClick = { saveChanges() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2B7A78)
                                    ),
                                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                                ) {
                                    Text("Enregistrer")
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { showDeleteDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFE53935)
                                    ),
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Delete")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Annuler")
                                }

                                Button(
                                    onClick = { isEditing = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2B7A78)
                                    ),
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Modifier")
                                }

                                Button(
                                    onClick = { completeIntervention() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF008D38)
                                    ),
                                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Complete")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Terminer")
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Cette intervention est terminée",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF008D38),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Confirmer la suppression",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Êtes-vous sûr de vouloir supprimer cette intervention ? Cette action est irréversible.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteIntervention()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Oui", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Non", color = Color.White)
                }
            }
        )
    }

    if (showSuccessToast) {
        SuccessToast(
            message = toastMessage,
            onDismiss = { showSuccessToast = false }
        )
    }
}