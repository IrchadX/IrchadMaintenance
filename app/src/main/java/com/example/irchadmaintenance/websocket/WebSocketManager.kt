package com.example.irchadmaintenance.websocket

import android.content.Context
import android.util.Log
import com.example.irchadmaintenance.data.Notification
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

object WebSocketManager {
    private lateinit var socket: Socket
    private val gson = Gson()
    private var reconnectAttempts = 0
    private const val MAX_RECONNECT_ATTEMPTS = 5
    private const val INITIAL_RECONNECT_DELAY = 1000L // 1 second

    private var serverUrl: String = "https:192.168.1.1:3000"
    private var onNotificationCallback: ((Notification) -> Unit)? = null

    fun initialize(context: Context, serverIp: String, port: Int = 8765) {
        serverUrl = "ws://$serverIp:$port"
    }

    fun connect(onNotificationReceived: (Notification) -> Unit) {
        if (::socket.isInitialized && socket.connected()) {
            Log.d("WebSocket", "Already connected")
            return
        }

        onNotificationCallback = onNotificationReceived

        try {
            val options = IO.Options().apply {
                reconnection = true
                reconnectionDelay = INITIAL_RECONNECT_DELAY
                reconnectionDelayMax = 5000L
                timeout = 10000L
            }

            socket = IO.socket(serverUrl, options)

            setupSocketListeners()
            socket.connect()

        } catch (e: URISyntaxException) {
            Log.e("WebSocket", "Invalid server URL: $serverUrl", e)
        } catch (e: Exception) {
            Log.e("WebSocket", "Connection error", e)
        }
    }

    private fun setupSocketListeners() {
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("WebSocket", "Connected to Raspberry Pi")
            reconnectAttempts = 0
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("WebSocket", "Connection error: ${args.firstOrNull()}")
            handleReconnection()
        }

        socket.on("alert") { args ->
            if (args.isNotEmpty()) {
                try {
                    val json = args[0] as JSONObject
                    val notification = gson.fromJson(json.toString(), Notification::class.java)
                    onNotificationCallback?.invoke(notification)
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing alert", e)
                }
            }
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("WebSocket", "Disconnected from Raspberry Pi")
            handleReconnection()
        }
    }

    private fun handleReconnection() {
        if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
            reconnectAttempts++
            val delay = INITIAL_RECONNECT_DELAY * (1 shl (reconnectAttempts - 1))
            Log.d("WebSocket", "Attempting to reconnect in ${delay}ms (attempt $reconnectAttempts)")
        } else {
            Log.e("WebSocket", "Max reconnection attempts reached")
        }
    }

    fun disconnect() {
        if (::socket.isInitialized) {
            socket.disconnect()
            onNotificationCallback = null
        }
    }

    fun isConnected(): Boolean {
        return ::socket.isInitialized && socket.connected()
    }
}