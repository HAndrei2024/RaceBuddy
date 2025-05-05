package com.example.racebuddy.ui.v2.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState("", ""))
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }

    fun onLoginButtonClick() {
        // TODO: Verify login
    }

    fun onSignUpTextClick() {

    }

    fun onSkipButtonClick() {

    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                LoginScreenViewModel()
            }
        }
    }
}

data class LoginScreenUiState(
    val email: String,
    val password: String,
    val errorMessage: Boolean = false,
    val loginSucces: Boolean = false
)