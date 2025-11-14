package com.example.githubviewer.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun onAuthCodeReceived(code: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Exchange code for token
                val token = exchangeCodeForToken(code)
                gitHubRepository.saveAccessToken(token)
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            gitHubRepository.clearAccessToken()
            _authState.value = AuthState.Unauthenticated
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = gitHubRepository.getAccessToken()
            _authState.value = if (token != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    private suspend fun exchangeCodeForToken(code: String): String {
        // This would call your backend or GitHub directly
        // For demo, we'll use a mock implementation
        return "mock_access_token_$code"
    }
}

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}