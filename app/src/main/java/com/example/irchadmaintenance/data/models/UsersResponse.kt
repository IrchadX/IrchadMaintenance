package com.example.irchadmaintenance.data.models
import com.google.gson.annotations.SerializedName
data class UsersResponse(
    val message: String,
    val data: List<HelperUser>
)

data class HelperUser(
    val id: Int,
    @SerializedName("helper_id")
    val helperId: Int,
    @SerializedName("user_id")
    val userId: Int,
    val state: String,
    @SerializedName("user_helper_user_user_idTouser")
    val userHelperUserUserIdTouser: User
)