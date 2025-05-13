package com.example.irchadmaintenance.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class User(
    val id: Int,

    @SerializedName("family_name")
    val familyName: String?,

    @SerializedName("first_name")
    val firstName: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    val password: String?,

    @SerializedName("userTypeId")
    val userTypeId: Int?,

    val email: String?,
    val sex: String?,
    val street: String?,
    val city: String?,
    @SerializedName("Identifier")
    val identifier: String?,

    @SerializedName("birthDate")
    val birthDate: String?,

    @SerializedName("userType")
    val userType: String?
)