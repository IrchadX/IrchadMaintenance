package com.example.irchadmaintenance.data


data class User(
    val userId : String,
    val name : String,
    val profilePicUrl : String,
    val notificationCount: Int
)
