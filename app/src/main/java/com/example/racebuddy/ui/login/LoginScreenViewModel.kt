package com.example.racebuddy.ui.login

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState("", ""))
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = username
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

    fun onLoginButtonPressed() {
        viewModelScope.launch {
            val loginResult = appRepository.verifyLogin(
                uiState.value.username,
                uiState.value.password
            )

            when(loginResult) {
                0 -> { onUsernameChange("Not correct!") }
                1 -> { onUsernameChange("Correct!") }
                else -> { onUsernameChange("Idk?!") }
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                LoginScreenViewModel(
                    appRepository = application.container.appRepository
                )
            }
        }
    }
}

data class LoginScreenUiState(
    val username: String,
    val password: String
)