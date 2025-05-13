package com.example.irchadmaintenance.viewmodels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.models.User
import com.example.irchadmaintenance.repository.AuthRepository
import com.example.irchadmaintenance.state.AuthUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AuthViewModel(
    val authRepository: AuthRepository = AuthRepository(appContext)
) : ViewModel() {

    private val _state = MutableStateFlow<AuthUIState>(AuthUIState.Idle)
    val state: StateFlow<AuthUIState> = _state.asStateFlow()

    private val _authState = MutableStateFlow<AuthUIState>(AuthUIState.Loading)
    val authState: StateFlow<AuthUIState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthUIState.Loading

            try {
                withContext(Dispatchers.IO) {
                    val response = authRepository.signIn(email, password)
                    authRepository.saveAuthInfo(response.user, response.token, email)
                    _user.value = response.user // Store the user object
                    _state.value = AuthUIState.Success(response)
                    _authState.value = AuthUIState.Authenticated(response.user.id.toString())
                }
            } catch (e: Exception) {
                _state.value = AuthUIState.Error(e.message ?: "Sign in failed")
                // Ensure authState is also updated on error
                _authState.value = AuthUIState.Unauthenticated
            }
        }
    }

    // Updated checkAuthStatus to handle errors properly
    private fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading
            Log.d("AuthViewModel", "Checking auth status...")

            withContext(Dispatchers.IO) {
                try {
                    val token = authRepository.getAuthToken().firstOrNull()
                    val userId = authRepository.getUserId().firstOrNull()

                    Log.d("AuthViewModel", "Token: ${token?.take(10)}..., UserId: $userId")

                    if (!token.isNullOrEmpty() && !userId.isNullOrEmpty()) {
                        val isValid = try {
                            authRepository.validateToken(token)
                        } catch (e: Exception) {
                            Log.e("AuthViewModel", "Token validation failed", e)
                            false
                        }

                        Log.d("AuthViewModel", "Token is valid: $isValid")

                        if (isValid) {
                            // Fetch user details using stored data
                            val familyName = authRepository.getUserFamilyName().firstOrNull() ?: ""
                            val firstName = authRepository.getUserFirstName().firstOrNull() ?: ""
                            val email = authRepository.getUserEmail().firstOrNull() ?: ""

                            // Reconstruct the User object
                            val user = User(
                                id = userId.toInt(),
                                familyName = familyName,
                                firstName = firstName,
                                email = email,
                                // Set other fields as necessary or fetch from API
                                phoneNumber = null,

                                userType = null
                            )
                            _user.value = user
                            _authState.value = AuthUIState.Authenticated(userId)
                        } else {
                            Log.d("AuthViewModel", "Clearing auth info - token invalid")
                            authRepository.clearAuthInfo()
                            _authState.value = AuthUIState.Unauthenticated
                        }
                    } else {
                        Log.d("AuthViewModel", "No token or userId found")
                        _authState.value = AuthUIState.Unauthenticated
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error checking auth status", e)
                    _authState.value = AuthUIState.Unauthenticated
                    authRepository.clearAuthInfo() // Clear potentially corrupt auth data
                }
            }
        }
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("AuthViewModel", "Signing out user")
                authRepository.clearAuthInfo()
                _authState.value = AuthUIState.Unauthenticated
                _state.value = AuthUIState.Idle
                _user.value = null
            }
        }
    }

    companion object {
        lateinit var appContext: Context
            private set

        fun initialize(context: Context) {
            appContext = context.applicationContext
        }
    }
}