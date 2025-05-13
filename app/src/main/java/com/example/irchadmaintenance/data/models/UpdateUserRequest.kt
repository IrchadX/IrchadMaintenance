package com.example.irchadmaintenance.data.models

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("family_name")
    val familyName: String? = null,
    val email: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)