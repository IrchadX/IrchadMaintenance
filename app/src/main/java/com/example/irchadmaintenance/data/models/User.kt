package com.example.irchadmaintenance.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
data class User(
    val id: Int? = null,
    val firstName: String? = null,
    val familyName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val userType: String? = null,

    // Additional fields for UI purposes
    val profilePicUrl: String? = null,
    val notificationCount: Int = 0)
