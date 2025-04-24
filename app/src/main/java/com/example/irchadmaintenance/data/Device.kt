package com.example.irchadmaintenance.data

data class Device(
    val id : String,
    val name: String,
    val status : String,
    val location : String,
    val distance : Float,
    val imageUrl : String? = null,
    val type : String,
    val userId : String,
    val macAddress : String,
    val softwareVersion : String,
    val activationDate : String
)
