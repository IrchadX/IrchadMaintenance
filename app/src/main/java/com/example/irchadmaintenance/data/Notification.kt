package com.example.irchadmaintenance.data

data class Notification(
    val id: Int,
    val deviceId: Int?,
    val title: String,
    val message: String,
    val timestamp: String,
    val zone: String,
    val status: String,
    val alertType: String,
    var isHandled: Boolean = false,
    val severity: String?
)