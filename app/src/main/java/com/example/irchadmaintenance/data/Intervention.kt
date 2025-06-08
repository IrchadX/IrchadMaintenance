package com.example.irchadmaintenance.data
import java.time.LocalDate

data class Intervention(
    val id: String,
    val deviceId: String,
    val userId: String,
    var title: String,
    var description: String,
    val location: String,
    val date: String,
    val type: InterventionType,
    var status: InterventionStatus
)

enum class InterventionStatus {
    EN_MAINTENANCE, DONE
}

enum class InterventionType {
    PREVENTIVE, CURATIVE
}