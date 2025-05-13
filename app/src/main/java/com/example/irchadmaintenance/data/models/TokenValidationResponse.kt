package com.example.irchadmaintenance.data.models

data class TokenValidationResponse(
    val valid: Boolean,
    val userId: String? = null
)