package com.example.irchadmaintenance.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.irchadmaintenance.network.LocationWebSocketListener
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class DeviceDetailsViewModel : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun startListeningForLocation() {
        if (webSocket != null) return

        val serverUrl = "wss://websocket-production-1b56.up.railway.app/"
        val request = Request.Builder().url(serverUrl).build()

        val listener = LocationWebSocketListener { newLocation ->
            _userLocation.value = newLocation
        }

        Log.d("ViewModel", "Connecting to WebSocket for LIVE location updates...")
        webSocket = client.newWebSocket(request, listener)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "Closing WebSocket connection.")
        webSocket?.close(1000, "Screen closed")
    }
}