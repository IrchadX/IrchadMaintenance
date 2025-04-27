package com.example.irchadmaintenance.data.api

import com.example.irchadmaintenance.data.api.models.CreateDeviceDto
import com.example.irchadmaintenance.data.api.models.DeviceApiModel
import com.example.irchadmaintenance.data.api.models.DeviceDiagnosticApiModel
import com.example.irchadmaintenance.data.api.models.UpdateDeviceDto
import retrofit2.Response
import retrofit2.http.*

interface DeviceApiService {
    @GET("devices")
    suspend fun getAllDevices(): List<DeviceApiModel>

    @GET("devices")
    suspend fun getDevicesByStatus(@Query("state") status: String): List<DeviceApiModel>

    @GET("devices/user/{userId}")
    suspend fun getDevicesByUser(@Path("userId") userId: Int): List<DeviceApiModel>

    @GET("devices/{id}")
    suspend fun getDeviceById(@Path("id") id: Int): DeviceApiModel

    @GET("devices/{id}/diagnostic")
    suspend fun runDiagnostic(@Path("id") id: Int): DeviceDiagnosticApiModel

    @POST("devices")
    suspend fun createDevice(@Body device: CreateDeviceDto): DeviceApiModel

    @PATCH("devices/{id}")
    suspend fun updateDevice(@Path("id") id: Int, @Body updateData: UpdateDeviceDto): DeviceApiModel

    @DELETE("devices/{id}")
    suspend fun deleteDevice(@Path("id") id: Int): Response<Unit>
}