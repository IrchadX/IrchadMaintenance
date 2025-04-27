package com.example.irchadmaintenance.data.repository

import android.util.Log
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.data.api.ApiClient
import com.example.irchadmaintenance.data.api.models.CreateDeviceDto
import com.example.irchadmaintenance.data.api.models.DeviceApiModel
import com.example.irchadmaintenance.data.api.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.data.api.models.UpdateDeviceDto

import java.text.SimpleDateFormat
import java.util.Locale

class DeviceRepository {
    private val apiService = ApiClient.deviceApiService

    suspend fun getAllDevices(): List<Device> {
        return apiService.getAllDevices().map { it.toDevice() }
    }

    suspend fun getDevicesByUser(userId: Int): List<Device> {
        return apiService.getDevicesByUser(userId).map { it.toDevice() }
    }

    suspend fun getDevicesByStatus(status: String): List<Device> {
        return apiService.getDevicesByStatus(status).map { it.toDevice() }
    }

    suspend fun getDeviceById(id: Int): Device {
        return apiService.getDeviceById(id).toDevice()
    }

    suspend fun runDiagnostic(id: Int): DeviceDiagnosticApiModel {
        return apiService.runDiagnostic(id)
    }

    suspend fun createDevice(device: CreateDeviceDto): Device {
        return apiService.createDevice(device).toDevice()
    }

    suspend fun updateDevice(id: Int, updateData: UpdateDeviceDto): Device {
        return apiService.updateDevice(id, updateData).toDevice()
    }

    suspend fun deleteDevice(id: Int): Boolean {
        val response = apiService.deleteDevice(id)
        return response.isSuccessful
    }

    private fun DeviceApiModel.toDevice(): Device {
        val activationDate = try {
            val pattern = "yyyy-MM-dd'T'HH:mm"
            val serverDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val userPattern = "dd MMMM, HH:mm"
            val userDateFormat = SimpleDateFormat(userPattern, Locale.getDefault())

            val defaultDate = serverDateFormat.parse(this.dateOfService.toString())
            if (defaultDate != null) {
                userDateFormat.format(defaultDate)
            } else {
                "Unknown date"
            }
        } catch (e: Exception) {
            Log.e("DeviceRepository", "Error parsing date: ${this.dateOfService}", e)
            "Unknown date"
        }

        return Device(
            id = this.id.toString(),
            name = this.deviceType?.type ?: "Unknown Device",
            status = this.stateType?.state ?: "Unknown",
            location = "Location data not available",
            distance = 0f,
            imageUrl = null,
            type = this.deviceType?.type ?: "Unknown Type",
            userId = this.userId?.toString() ?: "",
            macAddress = this.macAddress,
            softwareVersion = this.softwareVersion,
            activationDate = activationDate
        )
    }

}
