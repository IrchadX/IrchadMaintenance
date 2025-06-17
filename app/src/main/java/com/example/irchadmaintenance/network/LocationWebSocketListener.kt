
package com.example.irchadmaintenance.network

import android.util.Log
import kotlinx.serialization.Serializable
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


@Serializable
data class LocationUpdateMessage(
    val type: String,
    val data: LocationData
)

@Serializable
data class LocationData(
    val userId: String,
    val coords: Coordinates
)

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class BaseMessage(
    val type: String
)

@Serializable
data class RegisterMessage(
    val type: String = "register",
    val userId: String,
    val role: String = "subscriber"
)

@Serializable
data class SubscribeMessage(
    val type: String = "subscribe",
    val publisherId: String
)

class LocationWebSocketListener(
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionOpened: () -> Unit
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "✅ Connection Opened!")
        onConnectionOpened()
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Received raw message: $text")
        onMessageReceived(text)
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "❌ Connection Failure: ${t.message}", t)
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "🔌 Connection Closing: $reason")
    }
}