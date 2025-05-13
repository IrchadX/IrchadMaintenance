package com.example.irchadmaintenance.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.irchadmaintenance.api.Client
import com.example.irchadmaintenance.data.models.SignInRequest
import com.example.irchadmaintenance.data.models.SignInResponse
import com.example.irchadmaintenance.data.models.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import retrofit2.HttpException

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthRepository(private val context: Context) {
    private val authApi = Client.authApi

    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_FAMILY_NAME = stringPreferencesKey("user_family_name")
        val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun signIn(email: String, password: String): SignInResponse {
        try {
            val request = SignInRequest(email, password)
            val response = authApi.signIn(request)
            saveAuthInfo(response.user, response.token, email) // Pass the User object
            return response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").optString("error", "Authentication failed")
            } catch (ex: Exception) {
                "Authentication failed"
            }
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error occurred")
        }
    }


    suspend fun saveAuthInfo(user: User, token: String, email: String) {
        context.authDataStore.edit { preferences ->
            preferences[USER_ID] = user.id.toString()
            preferences[AUTH_TOKEN] = token
            preferences[USER_EMAIL] = email
            preferences[USER_FAMILY_NAME] = user.familyName ?: ""
            preferences[USER_FIRST_NAME] = user.firstName ?: ""
        }
    }
    fun getUserFamilyName(): Flow<String?> {
        return context.authDataStore.data.map { it[USER_FAMILY_NAME] }
    }

    fun getUserFirstName(): Flow<String?> {
        return context.authDataStore.data.map { it[USER_FIRST_NAME] }
    }
    fun getUserId(): Flow<String?> {
        return context.authDataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }


    fun getAuthToken(): Flow<String?> {
        return context.authDataStore.data.map { preferences ->
            preferences[AUTH_TOKEN]
        }
    }

    fun getUserEmail(): Flow<String?> {
        return context.authDataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    suspend fun clearAuthInfo() {
        context.authDataStore.edit { it.clear() }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun isLoggedIn(): Boolean {
        val token = getAuthToken().firstOrNull()
        return !token.isNullOrEmpty() && validateToken(token)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun validateToken(token: String): Boolean {
        return try {
            val response = authApi.validateToken("Bearer $token")
            response.valid
        } catch (e: Exception) {
            false
        }
    }
}