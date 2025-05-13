package com.example.irchadmaintenance.api


import com.example.irchadmaintenance.data.models.SignInRequest
import com.example.irchadmaintenance.data.models.SignInResponse
import com.example.irchadmaintenance.data.models.TokenValidationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse
    @GET("/auth/validate")
    suspend fun validateToken(@Header("Authorization") token: String): TokenValidationResponse

}