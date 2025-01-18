package com.example.racebuddy.ui.favorite

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
import com.example.racebuddy.ui.main.MainScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteScreenViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
//    private val _favoriteEventList = MutableStateFlow<List<Event>>(emptyList())
//    val favoriteEventList: StateFlow<List<Event>> = _favoriteEventList

    private val _athleteLoginId = userPreferencesRepository.athleteLoginId.map { athleteLoginId ->
        athleteLoginId
    }

    private val _uiState = MutableStateFlow(FavoriteScreenUiState(-1, emptyList()))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _athleteLoginId.collect { athleteLoginId ->
                _uiState.update { currentState ->
                    currentState.copy(
                        athleteId = athleteLoginId
                    )
                }
                if (athleteLoginId > 0) {
                    getFavoriteEvents(athleteLoginId).collect { events ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                favoriteEvents = events
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getFavoriteEvents(athleteId: Int): Flow<List<Event>> {
        return appRepository.getListOfFavoriteEvents(athleteId)
    }

    fun onFavoriteIconClick(
        athleteId: Int,
        eventId: Int,
        newFavoriteValue: Boolean) {
        if(newFavoriteValue) {
            viewModelScope.launch {
                appRepository.addFavoriteEvent(
                    athleteId = athleteId,
                    eventId = eventId
                )
            }
        }
        else {
            viewModelScope.launch {
                appRepository.removeFavoriteEvent(
                    athleteId = athleteId,
                    eventId = eventId
                )
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                FavoriteScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class FavoriteScreenUiState(
    val athleteId: Int,
    val favoriteEvents: List<Event>
)