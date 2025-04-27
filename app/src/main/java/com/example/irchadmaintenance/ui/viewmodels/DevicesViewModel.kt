package com.example.irchadmaintenance.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.api.models.CreateDeviceDto
import com.example.irchadmaintenance.data.api.models.UpdateDeviceDto
import com.example.irchadmaintenance.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// Update your DeviceViewModel with logging
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
                val result = repository.getDevicesByUser(userId)
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

    fun refreshDevices(userId: Int) {
        loadDevicesForUser(userId)
    }
}