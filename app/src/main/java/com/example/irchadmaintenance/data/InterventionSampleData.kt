package com.example.irchadmaintenance.data

object InterventionSampleData {
    val interventions = listOf(
        Intervention(
            id = "INT-001",
            deviceId = "LCSV-00124-AV",
            userId = "user001",
            title = "Maintenance préventive",
            description = "Vérification périodique des composants et mise à jour du firmware",
            location = "ESI, Oued Smar, Alger",
            date = "2025-04-15",
            type = InterventionType.PREVENTIVE,
            status = InterventionStatus.EN_MAINTENANCE
        ),
        Intervention(
            id = "INT-002",
            deviceId = "LBP-34690-LR",
            userId = "user001",
            title = "Réparation écran",
            description = "Remplacement de l'écran endommagé suite à une chute",
            location = "USTHB, Bab Ezzouar, Alger",
            date = "2025-03-22",
            type = InterventionType.CURATIVE,
            status = InterventionStatus.DONE
        ),
        Intervention(
            id = "INT-003",
            deviceId = "LCSV-00124-AV2",
            userId = "user001",
            title = "Calibration capteurs",
            description = "Calibration des capteurs de mouvement et de proximité",
            location = "Centre Commercial Ardis, El Harrach, Alger",
            date = "2025-04-10",
            type = InterventionType.PREVENTIVE,
            status = InterventionStatus.EN_MAINTENANCE
        ),
        Intervention(
            id = "INT-004",
            deviceId = "LCSV-00124-AV",
            userId = "user001",
            title = "Mise à jour logicielle",
            description = "Installation de la dernière version du firmware v2.3.1",
            location = "ESI, Oued Smar, Alger",
            date = "2025-02-28",
            type = InterventionType.PREVENTIVE,
            status = InterventionStatus.DONE
        )
    )
}