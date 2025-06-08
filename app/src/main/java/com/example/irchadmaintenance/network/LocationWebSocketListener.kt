package com.example.irchadmaintenance.network

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


@Serializable
data class Coords(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class WebSocketMessage(
    val type: String,
    val coords: Coords
)

class LocationWebSocketListener(
    private val onLocationUpdate: (LatLng) -> Unit
) : WebSocketListener() {

    private val json = Json { ignoreUnknownKeys = true }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "LIVE MODE: Connection Opened!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Received raw broadcast: $text")

        if (text.startsWith("Broadcast: ")) {
            val jsonString = text.substringAfter("Broadcast: ")
            try {
                val message = json.decodeFromString<WebSocketMessage>(jsonString)

                if (message.type == "location") {
                    val newLatLng = LatLng(message.coords.latitude, message.coords.longitude)
                    Log.d("WebSocket", "SUCCESS! Parsed location: $newLatLng")
                    onLocationUpdate(newLatLng)
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Error parsing location JSON: $jsonString", e)
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Connection Failure: ${t.message}", t)
    }
}