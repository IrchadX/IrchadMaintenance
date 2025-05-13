package com.example.irchadmaintenance.state

import com.example.irchadmaintenance.data.models.SignInResponse

sealed class AuthUIState {
    data object Loading : AuthUIState()
    data object Unauthenticated : AuthUIState()
    data class Authenticated(val userId: String) : AuthUIState()
    data class Error(val message: String) : AuthUIState()
    // data class SignUpSuccess(val message: String) : AuthUIState()
    object Idle : AuthUIState()
    data class Success(val response: SignInResponse) : AuthUIState()
    data class SignUpSuccess(val userId: Int) : AuthUIState()
}