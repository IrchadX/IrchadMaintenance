package com.example.irchadmaintenance.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.repositories.AlertRepository
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {
    private val _notifications = mutableStateListOf<Notification>()
    val notifications: List<Notification> = _notifications

    private val repository = AlertRepository()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val response = repository.fetchAlerts()
                if (response.isSuccessful && response.body() != null) {
                    _notifications.clear()
                    _notifications.addAll(response.body()!!)
                    Log.d("AlertViewModel", "Received alerts")
                }
            } catch (e: Exception) {
                Log.e("AlertViewModel", "Error fetching alerts", e)
            }
        }
    }

    fun markAllAsHandled() {
        _notifications.forEachIndexed { index, _ ->
            _notifications[index] = _notifications[index].copy(isHandled = true)
        }
    }

    fun getNotificationById(id: Int): Notification? {
        return notifications.find { it.id == id }
    }
}
