package com.example.irchadmaintenance.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.osmdroid.util.GeoPoint
import com.example.irchadmaintenance.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class DeviceDetailsViewModel : ViewModel() {

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
    private var isRegistered = false
    private var userToSubscribeTo: String? = null
    private val maintenancierId = "maintenancier_app_007"

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        if (webSocket != null) return
        val serverUrl = "wss://websocket-production-1b56.up.railway.app/"
        val request = Request.Builder().url(serverUrl).build()
        val listener = LocationWebSocketListener(
            onConnectionOpened = { registerAsSubscriber() },
            onMessageReceived = { text -> handleWebSocketMessage(text) }
        )
        Log.d("ViewModel", "Connecting to WebSocket...")
        webSocket = client.newWebSocket(request, listener)
    }

    private fun registerAsSubscriber() {
        val registerMsg = RegisterMessage(userId = maintenancierId)
        val jsonMessage = json.encodeToString(RegisterMessage.serializer(), registerMsg)
        Log.d("ViewModel", "--> Sending Register message: $jsonMessage")
        webSocket?.send(jsonMessage)
    }

    fun subscribeToUser(publisherId: String) {
        userToSubscribeTo = publisherId

        if (isRegistered) {
            sendSubscribeMessage()
        }
    }

    private fun sendSubscribeMessage() {
        userToSubscribeTo?.let { publisherId ->
            val subscribeMsg = SubscribeMessage(publisherId = publisherId)
            val jsonMessage = json.encodeToString(SubscribeMessage.serializer(), subscribeMsg)
            Log.d("ViewModel", "--> Sending Subscribe message for user: $publisherId")
            webSocket?.send(jsonMessage)
        }
    }

    private fun handleWebSocketMessage(text: String) {
        viewModelScope.launch {
            try {
                val baseMessage = json.decodeFromString(BaseMessage.serializer(), text)

                when (baseMessage.type) {

                    "registered" -> {
                        Log.d("ViewModel", "✅ Successfully registered with server.")
                        isRegistered = true
                        sendSubscribeMessage()
                    }

                    "location_update" -> {
                        val locationMessage = json.decodeFromString(LocationUpdateMessage.serializer(), text)
                        if (locationMessage.data.userId == userToSubscribeTo) {
                            val newGeoPoint = GeoPoint(
                                locationMessage.data.coords.latitude,
                                locationMessage.data.coords.longitude
                            )
                            _userLocation.value = newGeoPoint
                            Log.d("ViewModel", "✅ Updated location for '$userToSubscribeTo': $newGeoPoint")
                        }
                    }

                    "subscribed" -> {
                        Log.d("ViewModel", "✅ Successfully subscribed to updates.")
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error parsing message: $text", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isRegistered = false
        webSocket?.close(1000, "ViewModel cleared")
    }
}