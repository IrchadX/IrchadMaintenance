package com.example.irchadmaintenance.data

data class Device(
    val id : Int,
    val name: String,
    val status : String,
    val type : String,
    val userId : String,
    val macAddress : String,
    val softwareVersion : String,
    val serviceDate : String,
    val battery: String,
    val temperature: String,
    val signal: String
)
