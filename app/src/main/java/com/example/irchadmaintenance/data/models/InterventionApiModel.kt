package com.example.irchadmaintenance.data.models


import java.util.Date

data class InterventionApiModel(
    val id: Int,
    val device_id: Int?,
    val maintenancier_id: Int?,
    val scheduled_date: Date,
    val completion_date: Date?,
    val description: String?,
    val status: String?,
    val type: String?,
    val title: String?,
    val user: UserApiModel?,
    val location: String?
)
