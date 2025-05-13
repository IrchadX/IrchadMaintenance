package com.example.irchadmaintenance.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = AuthUIState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val token = authRepository.getAuthToken().firstOrNull()
                    val userId = authRepository.getUserId().firstOrNull()

                    if (!token.isNullOrEmpty() && !userId.isNullOrEmpty()) {
                        val isValid = authRepository.validateToken(token)

                        if (isValid) {
                            _authState.value = AuthUIState.Authenticated(userId)
                        } else {
                            authRepository.clearAuthInfo()
                            _authState.value = AuthUIState.Unauthenticated
                        }
                    } else {
                        _authState.value = AuthUIState.Unauthenticated
                    }
                } catch (e: Exception) {
                    _authState.value = AuthUIState.Error(e.message ?: "Authentication check failed")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthUIState.Loading

            try {
                withContext(Dispatchers.IO) {
                    val response = authRepository.signIn(email, password)

                    authRepository.saveAuthInfo(
                        userId = response.user.id.toString(),
                        token = response.token,
                        email = email
                    )
                    _state.value = AuthUIState.Success(response)
                    _authState.value = AuthUIState.Authenticated(response.user.id.toString())
                }
            } catch (e: Exception) {
                _state.value = AuthUIState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.clearAuthInfo()
                _authState.value = AuthUIState.Unauthenticated
                _state.value = AuthUIState.Idle
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
