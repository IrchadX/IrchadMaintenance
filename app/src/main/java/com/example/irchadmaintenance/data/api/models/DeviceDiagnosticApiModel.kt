package com.example.irchadmaintenance.data.api.models

data class DeviceDiagnosticApiModel(
    val batteryLevel: Int,
    val temperature: Int,
    val connectivity: String,
    val signalStrength: String,
    val status: String // 'ok', 'warning', or 'error'
)