package com.example.irchadmaintenance.data.api.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DeviceApiModel(
    val id: Int,
    @SerializedName("type_id") val typeId: Int,
    @SerializedName("state_type_id") val stateTypeId: Int,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("mac_address") val macAddress: String,
    @SerializedName("software_version") val softwareVersion: String,
    @SerializedName("date_of_service") val dateOfService: Date,
    @SerializedName("comm_state") val commState: Boolean,
    @SerializedName("connection_state") val connectionState: Boolean?,
    @SerializedName("battery_capacity") val batteryCapacity: Int,
    val price: Double?,
    @SerializedName("device_type") val deviceType: DeviceTypeApiModel?,
    @SerializedName("state_type") val stateType: StateTypeApiModel?,
    @SerializedName("user") val user: UserApiModel?
)