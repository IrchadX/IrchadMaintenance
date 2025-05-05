package com.example.irchadmaintenance.repositories
import com.example.irchadmaintenance.data.Notification
import com.example.irchadmaintenance.api.RetrofitClient
import retrofit2.Response
class AlertRepository {
    suspend fun fetchAlerts(): Response<List<Notification>> =
        RetrofitClient.apiService.getAlerts()
}