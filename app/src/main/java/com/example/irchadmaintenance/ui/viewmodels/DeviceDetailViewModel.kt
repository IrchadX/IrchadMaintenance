package com.example.irchadmaintenance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.api.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.data.repository.DeviceRepository
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

    fun runDiagnostic(deviceId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _diagnostic.value = repository.runDiagnostic(deviceId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to run diagnostic"
            } finally {
                _isLoading.value = false
            }
        }
    }
}