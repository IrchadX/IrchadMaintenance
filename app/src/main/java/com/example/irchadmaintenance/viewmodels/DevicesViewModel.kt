package com.example.irchadmaintenance.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.network.LocationWebSocketListener
import com.example.irchadmaintenance.repository.DeviceRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class DeviceViewModel(private val repository: DeviceRepository) : ViewModel() {
    private val _devices = mutableStateListOf<Device>()
    val devices: List<Device> get() = _devices

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    fun loadDevicesForUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("DeviceViewModel", "Attempting to fetch devices for user $userId")
                val result = repository.getAllDevices()
                Log.d("DeviceViewModel", "API Response: $result")
                _devices.clear()
                _devices.addAll(result)
                Log.d("DeviceViewModel", "Updated devices list, now contains ${_devices.size} items")
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error fetching devices", e)
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun loadDeviceById(deviceId: Int): Device? {
        return try {
            _isLoading.value = true
            repository.getDeviceById(deviceId)
        } catch (e: Exception) {
            Log.e("DeviceViewModel", "Error loading device $deviceId", e)
            null
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun runDiagnostic(id: Int): DeviceDiagnosticApiModel? {
        try {
            val response = repository.runDiagnostic(id)
            Log.d("DeviceViewModel", "Raw diagnostic response: $response")
            Log.d("DeviceViewModel", "Connectivity result: $response.connectionState )")

            // Vérifiez que tous les champs requis sont présents
            if (response.macAddress == null) {
                Log.w("DeviceViewModel", "Diagnostic response has null macAddress")
            }

            return DeviceDiagnosticApiModel(
                id = response.id,
                batteryLevel = response.batteryLevel?.replace("%", "") + "%" ?: "0%", // Protégez contre les nulls
                temperature = response.temperature ?: "35 °C",
                connectivity = response.connectivity ,
                macAddress = response.macAddress ?: "Non disponible", // Solution pour macAddress null
                softwareVersion = response.softwareVersion ?: "Non disponible",
                commState = response.commState ?: false,
            )
        } catch (e: Exception) {
            Log.e("DeviceViewModel", "Error running diagnostic for device $id", e)
            return null
        }
    }

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    // Modified to accept userId as parameter
    fun startListeningForLocation(userId: String, publisherId: String) {
        if (webSocket != null) return

        val subscriberId = "android_subscriber_$userId"
        val serverUrl = "wss://websocket-production-1b56.up.railway.app/"
        val request = Request.Builder().url(serverUrl).build()

        val listener = LocationWebSocketListener(
            subscriberId = subscriberId,
            publisherId = publisherId
        ) { newLocation ->
            _userLocation.value = newLocation
        }

        Log.d("ViewModel", "Connecting to WebSocket for location updates from publisher: $publisherId")
        webSocket = client.newWebSocket(request, listener)
    }

    fun stopListening() {
        webSocket?.close(1000, "Client disconnecting")
        webSocket = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "Closing WebSocket connection.")
        webSocket?.close(1000, "Screen closed")
    }

    fun refreshDevices(userId: Int) {
        loadDevicesForUser(userId)
    }
}