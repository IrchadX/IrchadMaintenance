package com.example.irchadmaintenance.websocket

import android.util.Log
import com.example.irchadmaintenance.data.Notification
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object WebSocketManager {
    private lateinit var socket: Socket
    private const val SOCKET_URL = "http://10.0.2.2:3000"
    private val gson = Gson()

    fun connect(onNotificationReceived: (Notification) -> Unit) {
        if (::socket.isInitialized && socket.connected()) return

        try {
            socket = IO.socket(SOCKET_URL)
            socket.io().reconnection(true) // auto-reconnect
            socket.connect()

            socket.on(Socket.EVENT_CONNECT) {
                Log.d("WebSocket", "Connected to server")
            }

            socket.on("alert") { args ->
                if (args.isNotEmpty()) {
                    try {
                        val json = args[0] as JSONObject
                        val notification = gson.fromJson(json.toString(), Notification::class.java)
                        onNotificationReceived(notification)
                    } catch (e: Exception) {
                        Log.e("WebSocket", "Error parsing alert: ${e.message}")
                    }
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("WebSocket", "Disconnected")
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Connection error: ${e.message}")
        }
    }

    fun disconnect() {
        if (::socket.isInitialized && socket.connected()) {
            socket.disconnect()
        }
    }

    fun isConnected(): Boolean {
        return ::socket.isInitialized && socket.connected()
    }
}
