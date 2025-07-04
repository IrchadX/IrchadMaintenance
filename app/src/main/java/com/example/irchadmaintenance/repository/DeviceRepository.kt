package com.example.irchadmaintenance.repository

import android.util.Log
import com.example.irchadmaintenance.data.Device
import com.example.irchadmaintenance.api.Client
import com.example.irchadmaintenance.data.models.CreateDeviceDto
import com.example.irchadmaintenance.data.models.DeviceApiModel
import com.example.irchadmaintenance.data.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.data.models.UpdateDeviceDto

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeviceRepository {
    private val apiService = Client.deviceApiService

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
            if (this.dateOfService != null) {
                if (this.dateOfService is Date) {
                    val userPattern = "dd MMMM, HH:mm"
                    val userDateFormat = SimpleDateFormat(userPattern, Locale.getDefault())
                    userDateFormat.format(this.dateOfService)
                } else {
                    val patterns = listOf(
                        "yyyy-MM-dd'T'HH:mm",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        "yyyy-MM-dd",
                        "EEE MMM dd HH:mm:ss zzz yyyy"
                    )

                    var parsed = false
                    var formattedDate = "Unknown date"

                    for (pattern in patterns) {
                        try {
                            val serverDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                            val parsedDate = serverDateFormat.parse(this.dateOfService.toString())
                            if (parsedDate != null) {
                                val userPattern = "dd MMMM, HH:mm"
                                val userDateFormat = SimpleDateFormat(userPattern, Locale.getDefault())
                                formattedDate = userDateFormat.format(parsedDate)
                                parsed = true
                                break
                            }
                        } catch (e: Exception) {
                            // Continue to next pattern
                        }
                    }

                    if (parsed) formattedDate else "Unknown date"
                }
            } else {
                "Unknown date"
            }
        } catch (e: Exception) {
            Log.e("DeviceRepository", "Error parsing date: ${this.dateOfService}", e)
            "Unknown date"
        }

        // Create user's full name from first_name and family_name
        val userFullName = when {
            this.user?.first_name != null && this.user?.family_name != null ->
                "${this.user.first_name} ${this.user.family_name}"
            this.user?.first_name != null -> this.user.first_name
            this.user?.family_name != null -> this.user.family_name
            else -> "Unknown User"
        }

        return Device(
            id = this.id.toString(),
            name = this.deviceType?.type ?: "Unknown Device",
            status = this.stateType?.state ?: "Unknown",
            location = "Centre Commercial Ardis, El Harrach, Alger", // Keep for backward compatibility
            userName = userFullName, // Add the user's full name
            distance = 0f,
            type = this.deviceType?.type ?: "Unknown Type",
            userId = this.userId?.toString() ?: "",
            macAddress = this.macAddress,
            softwareVersion = this.softwareVersion,
            activationDate = activationDate,
            imageName = this.imageName ?: "device_default"
        )
    }

    fun mapDatabaseResultToDiagnosticModel(dbResult: DeviceDiagnosticApiModel): DeviceDiagnosticApiModel {

        val batteryPercentage = "${dbResult.batteryLevel}%"


        val connectivityStatus = dbResult.connectivity

        val temperature = "35 °C"

        return DeviceDiagnosticApiModel(
            id = dbResult.id,
            batteryLevel = batteryPercentage,
            temperature = temperature,
            connectivity = connectivityStatus,
            macAddress = dbResult.macAddress,
            softwareVersion = dbResult.softwareVersion,
            commState = dbResult.commState,

            )
    }
}