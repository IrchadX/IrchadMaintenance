package com.example.irchadmaintenance.data.models

import java.util.Date
data class CreateInterventionDto(
    val device_id: Int?,
    val maintenancier_id: Int,
    val scheduled_date: Date,
    val description: String?,
    val status: String = "pending",
    val type: String,
    val title: String?,
    val location: String?
)