package com.example.irchadmaintenance.ui.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.irchadmaintenance.data.UserSampleData
import com.example.irchadmaintenance.ui.components.AppHeader
import com.example.irchadmaintenance.ui.components.BackConfirmationDialog
import com.example.irchadmaintenance.ui.components.FormTextField
import com.example.irchadmaintenance.ui.components.InterventionCalendar
import com.example.irchadmaintenance.ui.components.InterventionTypeToggle
import com.example.irchadmaintenance.ui.components.LocationIcon
import com.example.irchadmaintenance.ui.components.SuccessToast
import com.example.irchadmaintenance.viewmodels.InterventionViewModel
import java.time.LocalDate
import java.time.YearMonth

val TealColor = Color(0xFF2AA198)
val DarkTealColor = Color(0xFF004D40)
val WhiteColor = Color.White
val LightTealColor = Color(0xFFE0F2F1)
val DarkTealTextColor = Color(0xFF00766C)
val LightGrayBgColor = Color(0xFFF5F5F5)
val GrayTextColor = Color(0xFF616161)
val MediumGrayColor = Color(0xFFE0E0E0)

enum class InterventionType {
    PREVENTIVE, CURATIVE
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InterventionScreen(
    userId: String,
    navController: NavController,
    viewModel: InterventionViewModel
) {
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.error
    val success by viewModel.success

    var showSuccessToast by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("ESI, Oued Smar, Alger") }
    var description by remember { mutableStateOf("") }
    var interventionType by remember { mutableStateOf(InterventionType.PREVENTIVE) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var showBackConfirmationDialog by remember { mutableStateOf(false) }

    // Show success toast when intervention is created successfully
    LaunchedEffect(success) {
        if (success) {
            showSuccessToast = true
            viewModel.resetSuccess()
        }
    }

    BackHandler {
        showBackConfirmationDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val user = UserSampleData.users.find { it.userId == userId }

            AppHeader(
                user = user,
                navController = navController,
                title = "Intervention",
                default = false,
                warning = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                InterventionCalendar(
                    currentYearMonth = currentYearMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onPreviousMonthClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                    onNextMonthClick = { currentYearMonth = currentYearMonth.plusMonths(1) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val formattedDate = selectedDate?.toString() ?: ""

                FormTextField(
                    label = "Date sélectionnée",
                    value = formattedDate,
                    onValueChange = {},
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    label = "Ajouter un titre",
                    value = title,
                    onValueChange = { title = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    label = "Localisation",
                    value = location,
                    onValueChange = { location = it },
                    leadingIcon = { isFocused -> LocationIcon(isFocused) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    label = "Description",
                    value = description,
                    onValueChange = { description = it },
                    singleLine = false,
                    modifier = Modifier.height(150.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                InterventionTypeToggle(
                    selectedType = interventionType,
                    onTypeSelected = { interventionType = it }
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Show any error messages
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        if (selectedDate != null && title.isNotBlank()) {
                            viewModel.createIntervention(
                                userId = userId.toInt(),
                                selectedDate = selectedDate!!,
                                title = title,
                                location = location,
                                description = description,
                                interventionType = interventionType
                            )

                            // Fields will be reset on success via the LaunchedEffect
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF17252A),
                        contentColor = WhiteColor
                    ),
                    enabled = !isLoading && selectedDate != null && title.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = WhiteColor,
                            modifier = Modifier.height(24.dp)
                        )
                    } else {
                        Text(
                            "Ajouter",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(107.dp))
            }
        }
    }

    if (showSuccessToast) {
        SuccessToast(
            message = "Intervention ajoutée",
            onDismiss = {
                showSuccessToast = false

                // Reset form fields
                selectedDate = null
                title = ""
                location = "ESI, Oued Smar, Alger"
                description = ""
                interventionType = InterventionType.PREVENTIVE
                currentYearMonth = YearMonth.now()
            }
        )
    }

    if (showBackConfirmationDialog) {
        BackConfirmationDialog(
            onConfirm = { navController.popBackStack() },
            onDismiss = { showBackConfirmationDialog = false }
        )
    }
}