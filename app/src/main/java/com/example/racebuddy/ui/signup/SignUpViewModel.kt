package com.example.racebuddy.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.Athlete
import com.example.racebuddy.ui.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState("", "", "", ""))
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name
            )
        }
    }
    fun onSurnameChange(surname: String) {
        _uiState.update { currentState ->
            currentState.copy(
                surname = surname
            )
        }
    }

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

    fun onSignUpButtonPressed() {
        viewModelScope.launch {
            appRepository.createNewAccount(Athlete(
                name = uiState.value.name,
                surname = uiState.value.surname,
                username = uiState.value.username,
                password = uiState.value.password
            ))
            onNameChange("Updated?")
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                SignUpViewModel(
                    appRepository = application.container.appRepository
                )
            }
        }
    }
}

data class SignUpUiState(
    val name: String,
    val surname: String,
    val username: String,
    val password: String
)