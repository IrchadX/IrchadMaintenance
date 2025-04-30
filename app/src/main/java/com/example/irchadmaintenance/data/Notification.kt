package com.example.irchadmaintenance.data

data class Notification(
    val id: String,
    val deviceId : String,
    val title: String,
    val message: String,
    val timestamp: String,
    val alertType: String,
    var isRead: Boolean = false,
    val severity: NotificationSeverity = NotificationSeverity.INFO
)

enum class NotificationSeverity {
    INFO,
    WARNING,
    CRITICAL
}
