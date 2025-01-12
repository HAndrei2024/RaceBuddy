package com.example.racebuddy.ui.main

import android.database.sqlite.SQLiteException
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Main Screen should know about a user being logged in or not from datastore
private const val TAG = "MainScreenViewModel"

class MainScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState(-1, "initial"))
    val uiState = _uiState.asStateFlow()

    val athleteLoginId = userPreferencesRepository.athleteLoginId.map { athleteLoginId ->
        athleteLoginId
    }

    init {
        viewModelScope.launch {
            athleteLoginId.collect { athleteLoginId ->
                _uiState.update { currentState ->
                    currentState.copy(
                        athleteLoginId = athleteLoginId
                    )
                }
                getAthleteUsernameById(_uiState.value.athleteLoginId)
            }
        }
    }

    private suspend fun getAthleteUsernameById(id: Int) {
        val username = appRepository.getUsernameById(id)
        updateUsername(username = username)
    }

     private fun updateUsername(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                athleteUsername = username
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                MainScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class MainScreenUiState(
    val athleteLoginId: Int,
    val athleteUsername: String,
)