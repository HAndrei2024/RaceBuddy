package com.example.racebuddy.ui.v2.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState(""))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    athleteId = appRepository.getSupabaseLoggedInAthlete()
                )
            }

//            userPreferencesRepository.supabaseAthleteId.collect { athleteId ->
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        athleteId = athleteId,
//                        //eventList = appRepository.getListOfEvents("")
//                    )
//                }
//                if(athleteId.isNotEmpty()) {
//                    getAthleteUsernameById(_uiState.value.athleteLoginId)
//                    getFavoriteEvents(athleteLoginId).collect {events ->
//                        _uiState.update { currentState ->
//                            currentState.copy(
//                                favoriteEvents = events
//                            )
//                        }
//                    }
//                }


        }
    }

    fun updateAthleteId() {
        _uiState.update { currentState ->
            currentState.copy(
                athleteId = appRepository.getSupabaseLoggedInAthlete()
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
    val athleteId: String
)