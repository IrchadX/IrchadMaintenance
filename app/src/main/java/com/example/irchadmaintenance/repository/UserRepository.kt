package com.example.irchadmaintenance.repository


import android.content.Context
import com.example.irchadmaintenance.api.Client
import com.example.irchadmaintenance.data.models.HelperUser
import com.example.irchadmaintenance.data.models.UpdatePasswordRequest
import com.example.irchadmaintenance.data.models.UpdateUserRequest
import com.example.irchadmaintenance.data.models.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository(private val context: Context) {
    private val usersApi = Client.usersApi

    suspend fun getUserById(userId: String): Result<User> {
        return try {
            val response = usersApi.getUserById(userId)
            Result.success(response)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }




    suspend fun changePassword(request: UpdatePasswordRequest): Result<Unit> {
        return try {
            val response = usersApi.changePassword(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // In UserRepository.kt
    suspend fun updateUserInfos(userId: Int, firstName: String?, familyName: String?, email: String?, phoneNumber: String?): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val updateRequest = UpdateUserRequest(
                    firstName = firstName,
                    familyName = familyName,
                    email = email,
                    phoneNumber = phoneNumber
                )
                val response = usersApi.updateUser(userId, updateRequest)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error code: ${response.code()}"))
                }
            } catch (e: HttpException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: IOException) {
                Result.failure(Exception("Connection error: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unknown error: ${e.message}"))
            }
        }
    }




}