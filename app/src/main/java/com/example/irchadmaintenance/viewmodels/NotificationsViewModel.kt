package com.example.irchadmaintenance.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.data.NotificationSampleData


class NotificationsViewModel : ViewModel() {
    private val _notifications = mutableStateListOf<Notification>()
    val notifications: List<Notification> = _notifications

    init {
        // Initialize with sample data
        _notifications.addAll(NotificationSampleData.alerts)
    }

    fun markAsRead(notificationId: String) {
        val index = _notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            _notifications[index] = _notifications[index].copy(isRead = true)
        }
    }

    fun markAllAsRead() {
        _notifications.forEachIndexed { index, _ ->
            _notifications[index] = _notifications[index].copy(isRead = true)
        }
    }

    fun getNotificationById(id: String): Notification? {
        return notifications.find { it.id == id }
    }
}