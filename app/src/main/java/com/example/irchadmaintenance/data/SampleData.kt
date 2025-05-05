package com.example.irchadmaintenance.data

object SampleData {
    val devices = listOf(
        Device(
            id = 12401,
            name = "Lunettes intelligentes",
            status = "EN SERVICE",
            type = "Smart Glasses",
            userId = "USER-23894-AB",
            macAddress = "00:1A:7D:DA:71:13",
            softwareVersion = "v1.2.3",
            serviceDate = "2024-02-15",
            battery = "85%",
            temperature = "40 °C",
            signal = "bon réseau"
        ),
        Device(
            id = 34690,
            name = "Lecteur Braille portable",
            status = "EN PANNE",
            type = "Braille Reader",
            userId = "USER-23894-AB",
            macAddress = "00:1B:7E:DC:71:14",
            softwareVersion = "v2.1.0",
            serviceDate = "2023-09-10",
            battery = "85%",
            temperature = "40 °C",
            signal = "bon réseau"
        ),
        Device(
            id = 12402,
            name = "Lunettes intelligentes",
            status = "MAINTENANCE",
            type = "Smart Glasses",
            userId = "USER-23894-AB",
            macAddress = "00:1C:7F:DB:71:15",
            softwareVersion = "v1.2.4",
            serviceDate = "2023-11-01",
            battery = "85%",
            temperature = "40 °C",
            signal = "bon réseau"
        ),
        Device(
            id = 12403,
            name = "Lunettes intelligentes",
            status = "EN SERVICE",
            type = "Smart Glasses",
            userId = "USER-23894-AB",
            macAddress = "00:1D:7A:DB:71:16",
            softwareVersion = "v1.2.3",
            serviceDate = "2023-06-25",
            battery = "85%",
            temperature = "40 °C",
            signal = "bon réseau"
        ),
        Device(
            id = 34691,
            name = "Lecteur Braille portable",
            status = "EN PANNE",
            type = "Braille Reader",
            userId = "USER-23894-AB",
            macAddress = "00:1E:7D:DA:71:17",
            softwareVersion = "v2.0.5",
            serviceDate = "2022-08-30",
            battery = "85%",
            temperature = "40 °C",
            signal = "bon réseau"
        ),
        Device(
            id = 12404,
            name = "Lunettes intelligentes",
            status = "MAINTENANCE",
            type = "Smart Glasses",
            userId = "USER-23894-AB",
            macAddress = "00:1F:7C:DA:71:18",
            softwareVersion = "v1.3.0",
            serviceDate = "2024-01-18",
            battery = "85%",
            temperature = "40 °C",
            signal = "mauvais réseau"
        )
    )
}