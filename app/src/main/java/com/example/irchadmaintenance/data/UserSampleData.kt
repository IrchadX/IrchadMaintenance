package com.example.irchadmaintenance.data

object UserSampleData {
    val users = listOf(
        User(
            userId = "user001",
            name = "Akram Haddadou",
            profilePicUrl = "device",
            notificationCount = 5,
            // Added sample data for new fields
            firstName = "Akram",
            familyName = "Haddadou",
            phoneNumber = "+213 555 123 456",
            email = "akram.haddadou@example.com",
            identifier = "AKRAM-001"
        ),
        User(
            userId = "user002",
            name = "Sarah Brahim",
            profilePicUrl = "device",
            notificationCount = 3,
            firstName = "Sarah",
            familyName = "Brahim",
            phoneNumber = "+213 555 789 012",
            email = "sarah.brahim@example.com",
            identifier = "SARAH-002"
        ),
        User(
            userId = "user003",
            name = "Mehdi Allaoui",
            profilePicUrl = "device",
            notificationCount = 2,
            firstName = "Mehdi",
            familyName = "Allaoui",
            phoneNumber = "+213 555 345 678",
            email = "mehdi.allaoui@example.com",
            identifier = "MEHDI-003"
        ),
        User(
            userId = "user004",
            name = "Lina Kheira",
            profilePicUrl = "device",
            notificationCount = 0,
            firstName = "Lina",
            familyName = "Kheira",
            phoneNumber = "+213 555 901 234",
            email = "lina.kheira@example.com",
            identifier = "LINA-004"
        ),
        User(
            userId = "user005",
            name = "Karim Bendaoud",
            profilePicUrl = "device",
            notificationCount = 1,
            firstName = "Karim",
            familyName = "Bendaoud",
            phoneNumber = "+213 555 567 890",
            email = "karim.bendaoud@example.com",
            identifier = "KARIM-005"
        ),
        User(
            userId = "user006",
            name = "Yasmine Louati",
            profilePicUrl = "device",
            notificationCount = 7,
            firstName = "Yasmine",
            familyName = "Louati",
            phoneNumber = "+213 555 234 567",
            email = "yasmine.louati@example.com",
            identifier = "YASMINE-006"
        )
    )
}
