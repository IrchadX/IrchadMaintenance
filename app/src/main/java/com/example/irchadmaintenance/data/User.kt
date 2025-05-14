package com.example.irchadmaintenance.data

data class User(
    val userId: String,
    val name: String,
    val profilePicUrl: String,
    val notificationCount: Int,
    val firstName: String = "",
    val familyName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val identifier: String = ""
)
