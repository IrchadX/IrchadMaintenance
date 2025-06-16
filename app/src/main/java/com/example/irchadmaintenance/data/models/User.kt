package com.example.irchadmaintenance.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class User(
    val id: Int? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("family_name")
    val familyName: String? = null,

    val email: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("userType")
    val userType: String? = null,

    @SerializedName("profilePicUrl")
    val profilePicUrl: String? = null,

    val notificationCount: Int = 0
)
