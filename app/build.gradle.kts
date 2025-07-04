plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.irchadmaintenance"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.irchadmaintenance"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation (libs.threetenabp)

// OkHttp for logging and intercepting network requests
    implementation (libs.okhttp3.okhttp)
    implementation (libs.okhttp3.logging.interceptor.v4100)
// Coroutines for async operations
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation (libs.socket.io.client)
    implementation(libs.osmdroid.android)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material)
    implementation (libs.hilt.android)
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.datastore.preferences.core)
    implementation (libs.java.jwt)
    implementation ("io.socket:socket.io-client:2.1.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    /////////////// OSMDroid dependencies //////////////////////
    implementation("org.osmdroid:osmdroid-android:6.1.16")

    /////////////////////// WEBSOCKET DEPENDENCIES //////////////////////
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //////////////////////////JSON PARSING /////////////////////////////
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    ///////////////////// GOOGLE MAPS UTILITIES (LatLng) ///////////////////////
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}