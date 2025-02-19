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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteScreenViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = userPreferencesRepository.athleteLoginId
        .flatMapLatest { athleteLoginId ->
            appRepository.getListOfFavoriteEvents(athleteLoginId).map { events ->
                FavoriteScreenUiState(
                    athleteId = athleteLoginId,
                    favoriteEvents = events
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoriteScreenUiState(
                athleteId = -1,
                favoriteEvents = emptyList()
            )
        )

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