package com.example.irchadmaintenance.data

object NotificationSampleData {
    val alerts = listOf(
        Notification(
            id = "1",
            deviceId = "LCSV-00124-AV",
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = false,
            severity = NotificationSeverity.CRITICAL
        ),
        Notification(
            id = "2",
            deviceId = "LCSV-00124-AV",
            title = "Alerte de connectivité",
            message = "Dispositif Y ne répond plus depuis 15 min.",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = false,
            severity = NotificationSeverity.CRITICAL
        ),
        Notification(
            id = "3",
            deviceId = "LCSV-00124-AV",
            title = "Alerte Surcharge!",
            message = "Surcharge détectée sur le circuit principal (120%)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = false,
            severity = NotificationSeverity.CRITICAL
        ),
        Notification(
            id = "4",
            deviceId = "LCSV-00124-AV",
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = false,
            severity = NotificationSeverity.CRITICAL
        ),
        Notification(
            id = "5",
            deviceId = "LCSV-00124-AV",
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = false,
            severity = NotificationSeverity.CRITICAL
        ),
        Notification(
            id = "6",
            deviceId = "LCSV-00124-AV",
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = true,
            severity = NotificationSeverity.INFO
        ),
        Notification(
            id = "7",
            deviceId = "LCSV-00124-AV",
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            alertType = "Hypertempérature",
            isRead = true,
            severity = NotificationSeverity.WARNING
        )
    )
}
