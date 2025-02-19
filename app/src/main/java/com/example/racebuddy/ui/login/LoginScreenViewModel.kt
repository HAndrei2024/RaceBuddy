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
import androidx.navigation.NavController
import com.example.racebuddy.AppScreen
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteDao
import com.example.racebuddy.data.database.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
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

    fun updateLogInSucces(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                logInSucces = boolean
            )
        }
    }

    fun updateErrorMessage(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = boolean
            )
        }
    }

    fun onLoginButtonPressed() {
        viewModelScope.launch {
            try {
                val loginResult = appRepository.verifyLogin(
                    uiState.value.username,
                    uiState.value.password
                )
                val loginResultBoolean = loginResult != 0
                updateLogInSucces(loginResultBoolean)
                updateErrorMessage(!loginResultBoolean)
                userPreferencesRepository.saveAthleteLoginId(loginResult)
            } catch (e: Exception) {
                updateErrorMessage(true)
            }
        }
    }

    fun updateDataSourceOnSkipButtonClicked() {
        viewModelScope.launch {
            userPreferencesRepository.saveAthleteLoginId(-1)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                LoginScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class LoginScreenUiState(
    val username: String,
    val password: String,
    val errorMessage: Boolean = false,
    val logInSucces: Boolean = false
)