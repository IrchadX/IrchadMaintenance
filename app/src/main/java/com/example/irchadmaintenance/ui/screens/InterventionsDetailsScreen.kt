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
import com.example.irchadmaintenance.data.InterventionSampleData
import com.example.irchadmaintenance.data.InterventionStatus
import com.example.irchadmaintenance.data.InterventionType
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.BackConfirmationDialog
import com.example.irchadmaintenance.ui.components.FormTextField
import com.example.irchadmaintenance.ui.components.InterventionStatusChip
import com.example.irchadmaintenance.ui.components.SuccessToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InterventionDetailsScreen(
    userId: String,
    interventionId: String,
    navController: NavController
) {

    val interventionIndex = remember {
        InterventionSampleData.interventions.indexOfFirst { it.id == interventionId }
    }

    val intervention = if (interventionIndex >= 0)
        InterventionSampleData.interventions[interventionIndex]
    else null

    val user = UserSampleData.users.find { it.userId == userId }
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf(intervention?.title ?: "") }
    var description by remember { mutableStateOf(intervention?.description ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSuccessToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    var status by remember { mutableStateOf(intervention?.status) }
    val isInMaintenance = status == InterventionStatus.EN_MAINTENANCE

    fun showToastAndNavigateBack(message: String) {
        toastMessage = message
        showSuccessToast = true
        coroutineScope.launch {
            delay(1500)
            navController.popBackStack()
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
            warning = false
        )

        if (intervention != null) {
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
                    InterventionStatusChip(status = status ?: InterventionStatus.EN_MAINTENANCE)
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
                        text = title,
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
                    text = intervention.date,
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
                    text = intervention.location,
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
                    text = if (intervention.type == InterventionType.PREVENTIVE) "Préventive" else "Curative",
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
                        text = description,
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
                                    title = intervention.title
                                    description = intervention.description
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
                                onClick = {
                                    intervention.title = title
                                    intervention.description = description
                                    isEditing = false
                                    toastMessage = "Modifications enregistrées"
                                    showSuccessToast = true
                                },
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
                                onClick = { showCancelDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE53935)
                                ),
                                modifier = Modifier.weight(1f).padding(end = 8.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel")
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
                                onClick = {

                                    intervention.status = InterventionStatus.DONE
                                    status = InterventionStatus.DONE
                                    showToastAndNavigateBack("Intervention terminée")
                                },
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
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Intervention non trouvée")
            }
        }
    }

    if (showCancelDialog) {
        BackConfirmationDialog(
            onConfirm = {
                showToastAndNavigateBack("Intervention annulée")
                showCancelDialog = false
            },
            onDismiss = { showCancelDialog = false }
        )
    }

    if (showSuccessToast) {
        SuccessToast(
            message = toastMessage,
            onDismiss = { showSuccessToast = false }
        )
    }
}
