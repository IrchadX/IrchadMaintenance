package com.example.irchadmaintenance.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.notifications.NotificationHelper
import com.example.irchadmaintenance.repositories.AlertRepository
import com.example.irchadmaintenance.websocket.WebSocketManager // ⬅️ assure-toi d'avoir ce fichier
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {
    private val _notifications = mutableStateListOf<Notification>()
    val notifications: List<Notification> = _notifications

    private val repository = AlertRepository()

    init {
        fetchNotifications()               // Chargement initial // Écoute des alertes en temps réel
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

     fun listenForRealTimeAlerts(context: Context) {
        WebSocketManager.connect { newAlert ->
            _notifications.add(0, newAlert)

            // Afficher une notification système
            NotificationHelper.showNotification(
                context,
                title = "Nouvelle alerte",
                message = newAlert.message // ou un autre champ
            )
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
