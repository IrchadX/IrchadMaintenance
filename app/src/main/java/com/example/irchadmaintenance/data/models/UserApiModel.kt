package com.example.irchadmaintenance.data.models

data class UserApiModel(
    val id: Int,
    val name: String,
    val family_name: String?,
    val first_name: String?,
    val email: String
)
