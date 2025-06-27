 Open Project in Android Studio
Launch Android Studio (Hedgehog or later recommended)

Select "Open" → Navigate to the cloned repo folder

Wait for Gradle sync to finish (auto-triggered)
Network Configuration
Step 1: Update Base URL
Open the API client file: app/src/main/java/com/example/irchadmaintenance/api/Client.kt
Replace the base_url with your local network IP:  private const val BASE_URL = "http://192.168.93.216:3000"
 Network Requirements
 Both devices must be connected to the same Wi-Fi/LAN network
 For emulators, use http://10.0.2.2:3000/
Last  Step : Run the Backend Server
