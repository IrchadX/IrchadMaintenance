package com.example.irchadmaintenance.data.models

data class DeviceDiagnosticApiModel(
    val id: Int,
    val batteryLevel: String,
    val temperature: String,
    val connectivity: String,
    val macAddress: String?,
    val softwareVersion: String,
    val commState: Boolean=true,

)
