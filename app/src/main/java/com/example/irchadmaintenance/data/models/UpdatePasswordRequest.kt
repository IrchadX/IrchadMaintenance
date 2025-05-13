package com.example.irchadmaintenance.data.models

data class UpdatePasswordRequest(
    val userId: Int,
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)