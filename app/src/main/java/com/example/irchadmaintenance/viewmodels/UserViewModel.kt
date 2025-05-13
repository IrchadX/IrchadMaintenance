package com.example.irchadmaintenance.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.irchadmaintenance.data.models.HelperUser
import com.example.irchadmaintenance.data.models.UpdatePasswordRequest
import com.example.irchadmaintenance.data.models.User
import com.example.irchadmaintenance.repository.UserRepository
import com.example.irchadmaintenance.viewmodels.AuthViewModel.Companion.appContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: UserRepository = UserRepository(appContext)
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()


    private val _usersState = MutableStateFlow<UsersState>(UsersState.Initial)
    val usersState: StateFlow<UsersState> = _usersState.asStateFlow()

    fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val result = userRepository.getUserById(userId)
                    _user.value = result.getOrNull()
                }
            } catch (e: Exception) {
                _user.value = null
            }
        }
    }


    fun loadUsersForHelper(helperId: String) {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading

            try {
                val result = userRepository.getUsersForHelper(helperId)
                result.fold(
                    onSuccess = { users ->
                        _usersState.value = if (users.isEmpty()) {
                            UsersState.Empty
                        } else {
                            UsersState.Success(users)
                        }
                    },
                    onFailure = { error ->
                        _usersState.value = UsersState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _usersState.value = UsersState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun loadUserRequestsForHelper(helperId: String) {
        viewModelScope.launch {
            _usersState.value = UsersState.Loading

            try {
                val result = userRepository.getPendingRequestsForHelper(helperId)
                result.fold(
                    onSuccess = { users ->
                        _usersState.value = if (users.isEmpty()) {
                            UsersState.Empty
                        } else {
                            UsersState.Success(users)
                        }
                    },
                    onFailure = { error ->
                        _usersState.value = UsersState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _usersState.value = UsersState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun acceptRequest(id: Int) {
        viewModelScope.launch {
            try {
                userRepository.acceptRequest(id)

                val currentUserId = _user.value?.id
                currentUserId?.let { loadUserRequestsForHelper(it.toString()) }
            } catch (e: Exception) {
                // Handle error (log, state update, etc.)
            }
        }
    }

    fun declineRequest(id: Int) {
        viewModelScope.launch {
            try {
                userRepository.declineRequest(id)
                // Refresh the list after declining
                val currentUserId = _user.value?.id
                currentUserId?.let { loadUserRequestsForHelper(it.toString()) }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private val _passwordChangeState = MutableStateFlow<PasswordChangeState>(PasswordChangeState.Idle)
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState.asStateFlow()

    fun changePassword(
        userId: Int,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        viewModelScope.launch {
            _passwordChangeState.value = PasswordChangeState.Loading

            val request = UpdatePasswordRequest(
                userId, currentPassword, newPassword, confirmNewPassword
            )

            val result = runCatching {
                userRepository.changePassword(request)
            }

            if (result.isSuccess) {
                _passwordChangeState.value = PasswordChangeState.Success
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                _passwordChangeState.value = PasswordChangeState.Error(errorMsg)
            }
        }
    }


    private val _userUpdateState = MutableStateFlow<UserUpdateState>(UserUpdateState.Idle)
    val userUpdateState: StateFlow<UserUpdateState> = _userUpdateState.asStateFlow()

    fun updateUserInfos(
        userId: Int,
        firstName: String? = null,
        familyName: String? = null,
        email: String? = null,
        phoneNumber: String? = null
    ) {
        viewModelScope.launch {
            _userUpdateState.value = UserUpdateState.Loading

            val result = runCatching {
                userRepository.updateUserInfos(
                    userId,
                    firstName,
                    familyName,
                    email,
                    phoneNumber
                )
            }

            if (result.isSuccess) {
                _userUpdateState.value = UserUpdateState.Success
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                _userUpdateState.value = UserUpdateState.Error(errorMsg)
            }
        }
    }


    fun resetPasswordChangeState() {
        _passwordChangeState.value = PasswordChangeState.Idle
    }
    sealed class UsersState {
        object Initial : UsersState()
        object Loading : UsersState()
        object Empty : UsersState()
        data class Success(val users: List<HelperUser>) : UsersState()
        data class Error(val message: String) : UsersState()
    }

    sealed class UserUpdateState {
        object Idle : UserUpdateState()
        object Loading : UserUpdateState()
        object Success : UserUpdateState()
        data class Error(val message: String) : UserUpdateState()
    }


    sealed class PasswordChangeState {
        object Idle : PasswordChangeState()
        object Loading : PasswordChangeState()
        object Success : PasswordChangeState()
        data class Error(val message: String) : PasswordChangeState()
    }


}