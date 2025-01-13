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
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.login.LoginScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
//    val mainScreenEventListStateFlow: StateFlow<List<Event>> =
//        appRepository.getListOfEvents(_uiState.value.searchString).map { it }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = emptyList()
//            )
    val athleteLoginId = userPreferencesRepository.athleteLoginId.map { athleteLoginId ->
        athleteLoginId
    }

    init {
        viewModelScope.launch {
            athleteLoginId.collect { athleteLoginId ->
                _uiState.update { currentState ->
                    currentState.copy(
                        athleteLoginId = athleteLoginId,
                        eventList = appRepository.getListOfEvents("")
                    )
                }
                if(athleteLoginId > 0) {
                    getAthleteUsernameById(_uiState.value.athleteLoginId)
                }
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

    fun onSearchValueChange(searchString: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchString = searchString,
                eventList = appRepository.getListOfEvents(searchString)
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
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MainScreenUiState(
    val athleteLoginId: Int,
    val athleteUsername: String,
    val searchString: String = "",
    val eventList: Flow<List<Event>> = flowOf(emptyList())
)