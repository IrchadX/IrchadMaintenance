package com.example.irchadmaintenance.data

object NotificationSampleData {
    val alerts = listOf(
        Notification(
            id = 1,
            deviceId = 124,
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            zone = "Zone Moteur Principal",
            status = "Nouveau",
            alertType = "Hypertempérature",
            isHandled = false,
            severity = "Critique"
        ),
        Notification(
            id = 2,
            deviceId = 124,
            title = "Alerte de connectivité",
            message = "Dispositif Y ne répond plus depuis 15 min.",
            timestamp = "15h",
            zone = "Zone Communication",
            status = "En cours",
            alertType = "Connectivité",
            isHandled = false,
            severity = "Critique"
        ),
        Notification(
            id = 3,
            deviceId = 124,
            title = "Alerte Surcharge!",
            message = "Surcharge détectée sur le circuit principal (120%)!",
            timestamp = "15h",
            zone = "Zone Électrique",
            status = "Nouveau",
            alertType = "Surcharge",
            isHandled = false,
            severity = "Critique"
        ),
        Notification(
            id = 4,
            deviceId = 124,
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            zone = "Zone Moteur Auxiliaire",
            status = "Nouveau",
            alertType = "Hypertempérature",
            isHandled = false,
            severity = "Critique"
        ),
        Notification(
            id = 5,
            deviceId = 125,
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            zone = "Zone Moteur Principal",
            status = "Nouveau",
            alertType = "Hypertempérature",
            isHandled = false,
            severity = "Critique"
        ),
        Notification(
            id = 6,
            deviceId = 126,
            title = "Alert Température!",
            message = "Temperature du moteur au-delà du seuil critique (85°C)!",
            timestamp = "15h",
            zone = "Zone Moteur Principal",
            status = "Résolu",
            alertType = "Hypertempérature",
            isHandled = true,
            severity = "Modéré"
        ),
        Notification(
            id = 7,
            deviceId = null,
            title = "Alert Système!",
            message = "Maintenance système programmée dans 2 heures.",
            timestamp = "15h",
            zone = "Système Général",
            status = "Information",
            alertType = "Maintenance",
            isHandled = true,
            severity = "Mineur"
        )
    )
}