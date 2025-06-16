package com.example.irchadmaintenance.network

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

data class Coords(
    val latitude: Double,
    val longitude: Double
)

data class LocationData(
    val userId: String,
    val coords: Coords,
    val timestamp: String
)

data class WebSocketMessage(
    val type: String,
    val userId: String? = null,
    val role: String? = null,
    val publisherId: String? = null,
    val message: String? = null,
    val data: LocationData? = null,
    val currentLocation: LocationData? = null,
    val coords: Coords? = null // For outgoing registration messages
)

class LocationWebSocketListener(
    private val subscriberId: String,
    private val publisherId: String,
    private val onLocationUpdate: (LatLng) -> Unit
) : WebSocketListener() {

    private val gson = Gson()
    private var isRegistered = false
    private var isSubscribed = false

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "Connection opened! Registering as subscriber...")

        // Register as subscriber
        val registerMessage = WebSocketMessage(
            type = "register",
            userId = subscriberId,
            role = "subscriber"
        )

        val jsonString = gson.toJson(registerMessage)
        Log.d("WebSocket", "Sending registration: $jsonString")
        webSocket.send(jsonString)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Received message: $text")

        try {
            val message = gson.fromJson(text, WebSocketMessage::class.java)

            when (message.type) {
                "registered" -> {
                    Log.d("WebSocket", "Successfully registered as ${message.role} with ID: ${message.userId}")
                    isRegistered = true

                    // Now subscribe to the publisher
                    subscribeToPublisher(webSocket)
                }

                "subscribed" -> {
                    Log.d("WebSocket", "Successfully subscribed to publisher: ${message.publisherId}")
                    isSubscribed = true

                    // If there's a current location, update immediately
                    message.currentLocation?.let { location ->
                        val newLatLng = LatLng(location.coords.latitude, location.coords.longitude)
                        Log.d("WebSocket", "Received current location: $newLatLng")
                        onLocationUpdate(newLatLng)
                    }
                }

                "location_update" -> {
                    message.data?.let { locationData ->
                        val newLatLng = LatLng(locationData.coords.latitude, locationData.coords.longitude)
                        Log.d("WebSocket", "Location update from ${locationData.userId}: $newLatLng at ${locationData.timestamp}")
                        onLocationUpdate(newLatLng)
                    }
                }

                "error" -> {
                    Log.e("WebSocket", "Server error: ${message.message}")
                }

                else -> {
                    Log.d("WebSocket", "Unhandled message type: ${message.type}")
                }
            }

        } catch (e: Exception) {
            Log.e("WebSocket", "Error parsing message: $text", e)
        }
    }

    private fun subscribeToPublisher(webSocket: WebSocket) {
        if (!isRegistered) {
            Log.w("WebSocket", "Cannot subscribe - not registered yet")
            return
        }

        val subscribeMessage = WebSocketMessage(
            type = "subscribe",
            publisherId = publisherId
        )

        val jsonString = gson.toJson(subscribeMessage)
        Log.d("WebSocket", "Subscribing to publisher: $jsonString")
        webSocket.send(jsonString)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Connection closing: $code - $reason")
        isRegistered = false
        isSubscribed = false
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Connection closed: $code - $reason")
        isRegistered = false
        isSubscribed = false
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Connection failure: ${t.message}", t)
        isRegistered = false
        isSubscribed = false
    }
}