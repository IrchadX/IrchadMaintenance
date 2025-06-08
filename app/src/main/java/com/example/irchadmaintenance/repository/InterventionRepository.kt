package com.example.irchadmaintenance.repository

import android.util.Log
import com.example.irchadmaintenance.api.Client
import com.example.irchadmaintenance.data.models.CreateInterventionDto
import com.example.irchadmaintenance.data.models.Intervention
import com.example.irchadmaintenance.data.models.InterventionApiModel
import com.example.irchadmaintenance.data.models.UpdateInterventionDto
import com.example.irchadmaintenance.ui.screens.InterventionType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class InterventionRepository {
    private val apiService = Client.interventionApiService

    suspend fun getAllInterventions(): List<Intervention> {
        return apiService.getAllInterventions().map { it.toIntervention() }
    }

    suspend fun getInterventionsByStatus(status: String): List<Intervention> {
        return apiService.getInterventionsByStatus(status).map { it.toIntervention() }
    }

    suspend fun getInterventionById(id: Int): Intervention {
        return apiService.getInterventionById(id).toIntervention()
    }

    suspend fun updateIntervention(id: Int, title: String, description: String): Intervention {
        try {
            // FIXED: Use specific DTO instead of Map
            val updateDto = UpdateInterventionDto(
                title = title,
                description = description
            )
            val response = apiService.updateIntervention(id, updateDto)
            Log.d("InterventionRepository", "Updated intervention: $response")
            return response.toIntervention()
        } catch (e: Exception) {
            Log.e("InterventionRepository", "Error updating intervention", e)
            throw e
        }
    }

    suspend fun createIntervention(
        userId: Int,
        scheduledDate: LocalDate,
        title: String,
        location: String,
        description: String,
        deviceId: Int?,
        interventionType: InterventionType
    ): Intervention {
        try {
            val type = when (interventionType) {
                InterventionType.PREVENTIVE -> "technique"
                InterventionType.CURATIVE -> "Non_technique"
            }

            val date = Date.from(scheduledDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

            val createDto = CreateInterventionDto(
                device_id = deviceId,
                maintenancier_id = userId,
                scheduled_date = date,
                description = description,
                status = "pending",
                type = type,
                title = title,
                location = location
            )

            val response = apiService.createIntervention(createDto)
            Log.d("InterventionRepository", "Created intervention: $response")
            return response.toIntervention()
        } catch (e: Exception) {
            Log.e("InterventionRepository", "Error creating intervention", e)
            throw e
        }
    }
    suspend fun deleteIntervention(id: Int): Boolean {
        return try {
            val response = apiService.deleteIntervention(id)
            Log.d("InterventionRepository", "Deleted intervention with ID: $id")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("InterventionRepository", "Error deleting intervention", e)
            throw e
        }
    }
    suspend fun completeIntervention(id: Int): Intervention {
        return apiService.completeIntervention(id).toIntervention()
    }

    private fun InterventionApiModel.toIntervention(): Intervention {
        val dateFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())

        val scheduledDateFormatted = try {
            dateFormat.format(this.scheduled_date)
        } catch (e: Exception) {
            "Date non disponible"
        }

        val completionDateFormatted = try {
            this.completion_date?.let { dateFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }

        return Intervention(
            id = this.id.toString(),
            deviceId = this.device_id?.toString(),
            maintenancerId = this.maintenancier_id?.toString() ?: "",
            scheduledDate = scheduledDateFormatted,
            completionDate = completionDateFormatted,
            description = this.description ?: "",
            status = this.status ?: "pending",
            type = this.type ?: "technique",
            title = this.title ?: "",
            location = this.location ?: "ESI, Oued Smar, Alger"
        )
    }
}