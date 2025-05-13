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
    @GET("aidant/UsersForHelper/{id}")
    suspend fun getUsersForHelper(@Path("id") userId: String): UsersResponse

    @GET("aidant/pending-requests/{id}")
    suspend fun getPendingRequestsForHelper(@Path("id") userId: String): UsersResponse

    @POST("aidant/acceptpairing/{id}")
    suspend fun acceptPairingRequest(@Path("id") id: Int)

    @POST("aidant/declinepairing/{id}")
    suspend fun declinePairingRequest(@Path("id") id: Int)

    @PATCH("users/change-password")
    suspend fun changePassword(@Body request: UpdatePasswordRequest): Response<Unit>

    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<Unit>
}