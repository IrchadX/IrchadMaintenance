package com.example.irchadmaintenance.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceDetailViewModel : ViewModel() {
    private val repository = DeviceRepository()

    private val _device = MutableStateFlow<Device?>(null)
    val device: StateFlow<Device?> = _device.asStateFlow()

    private val _diagnostic = MutableStateFlow<DeviceDiagnosticApiModel?>(null)
    val diagnostic: StateFlow<DeviceDiagnosticApiModel?> = _diagnostic.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadDevice(deviceId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _device.value = repository.getDeviceById(deviceId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load device details"
            } finally {
                _isLoading.value = false
            }
        }
    }
    suspend fun runDiagnostic(id: Int): DeviceDiagnosticApiModel? {
        try {
            val response = repository.runDiagnostic(id)
            Log.d("DeviceViewModel", "Raw diagnostic response: $response")
            //Log.d("DeviceViewModel", "commState: ${response.commState}, connectionState: ${response.connectionState}")
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
                macAddress = response.macAddress ?: "Non disponible",
                softwareVersion = response.softwareVersion ?: "Non disponible",
                commState = response.commState ?: false,

                )
        } catch (e: Exception) {
            Log.e("DeviceViewModel", "Error running diagnostic for device $id", e)
            return null
        }
    }



}