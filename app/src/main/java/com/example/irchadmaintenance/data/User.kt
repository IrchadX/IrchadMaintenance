package com.example.irchadmaintenance.data

import android.app.Notification
data class User(
    val userId : String,
    val name : String,
    val profilePicUrl : String,
    val notificationCount: Int
)
