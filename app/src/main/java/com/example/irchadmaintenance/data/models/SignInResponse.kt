package com.example.irchadmaintenance.data.models
data class SignInResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String,
    val user: User
)