package com.example.irchadmaintenance.data.models

data class Intervention(
    val id: String,
    val deviceId: String?,
    val maintenancerId: String,
    val scheduledDate: String,
    val completionDate: String?,
    val description: String?,
    val status: String,
    val type: String,
    val title: String?,
    val location: String
)