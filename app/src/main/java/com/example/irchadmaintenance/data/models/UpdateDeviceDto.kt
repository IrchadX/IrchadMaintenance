package com.example.irchadmaintenance.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UpdateDeviceDto(
    @SerializedName("type_id") val typeId: Int? = null,
    @SerializedName("state_type_id") val stateTypeId: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("mac_address") val macAddress: String? = null,
    @SerializedName("software_version") val softwareVersion: String? = null,
    @SerializedName("comm_state") val commState: Boolean? = null,
    @SerializedName("connection_state") val connectionState: Boolean? = null,
    @SerializedName("battery_capacity") val batteryCapacity: Int? = null,
    val price: Double? = null,
    @SerializedName("date_of_service") val dateOfService: Date? = null
)