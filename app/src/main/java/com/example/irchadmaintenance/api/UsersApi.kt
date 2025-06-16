package com.example.irchadmaintenance.api

import com.example.irchadmaintenance.data.models.UpdatePasswordRequest
import com.example.irchadmaintenance.data.models.UpdateUserRequest
import com.example.irchadmaintenance.data.models.User
import com.example.irchadmaintenance.data.models.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UsersApi {
    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: String
    ): User

    @PATCH("users/change-password")
    suspend fun changePassword(@Body request: UpdatePasswordRequest): Response<Unit>

    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<Unit>
}