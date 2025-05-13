package com.example.irchadmaintenance.api

import com.example.irchadmaintenance.data.Notification
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("alerts/")
    suspend fun getAlerts(): Response<List<Notification>>
}