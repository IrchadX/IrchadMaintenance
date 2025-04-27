package com.example.irchadmaintenance.data.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://192.168.221.216:3000/"



    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("ApiClient", "Making request to: ${request.url}")
            try {
                val response = chain.proceed(request)
                Log.d("ApiClient", "Response code: ${response.code}")
                response
            } catch (e: Exception) {
                Log.e("ApiClient", "Request failed", e)
                throw e
            }
        }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // Note the quotes around 'Z'
        .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
            override fun deserialize(
                json: JsonElement,
                typeOfT: Type,
                context: JsonDeserializationContext
            ): Date {
                val formats = listOf(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    "yyyy-MM-dd"
                )

                formats.forEach { format ->
                    try {
                        return SimpleDateFormat(format, Locale.getDefault()).parse(json.asString)
                    } catch (e: Exception) {
                        // Try next format
                    }
                }
                Log.e("ApiClient", "Failed to parse date: ${json.asString}")
                return Date() // Fallback to current date
            }
        })
        .create()
    val deviceApiService: DeviceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DeviceApiService::class.java)
    }
}