package com.example.irchadmaintenance.api

import com.example.irchadmaintenance.data.models.CreateInterventionDto
import com.example.irchadmaintenance.data.models.InterventionApiModel
import retrofit2.Response
import retrofit2.http.*

interface InterventionApiService {
    @GET("interventions")
    suspend fun getAllInterventions(): List<InterventionApiModel>

    @GET("interventions")
    suspend fun getInterventionsByStatus(@Query("status") status: String): List<InterventionApiModel>

    @GET("interventions/{id}")
    suspend fun getInterventionById(@Path("id") id: Int): InterventionApiModel

    @POST("interventions")
    suspend fun createIntervention(@Body intervention: CreateInterventionDto): InterventionApiModel

    @PATCH("interventions/{id}")
    suspend fun updateIntervention(@Path("id") id: Int, @Body intervention: Map<String, Any>): InterventionApiModel

    @POST("interventions/{id}/complete")
    suspend fun completeIntervention(@Path("id") id: Int): InterventionApiModel
}