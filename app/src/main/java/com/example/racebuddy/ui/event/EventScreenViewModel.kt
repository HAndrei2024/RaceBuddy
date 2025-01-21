package com.example.racebuddy.ui.event

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.data.database.Result
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.favorite.FavoriteScreenUiState
import com.example.racebuddy.ui.favorite.FavoriteScreenViewModel
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

class EventScreenViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val uiState = userPreferencesRepository.athleteLoginId
//        .flatMapLatest { athleteLoginId ->
//            appRepository.getEvent(athleteLoginId).map { events ->
//                FavoriteScreenUiState(
//                    athleteId = athleteLoginId,
//                    favoriteEvents = events
//                )
//            }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = FavoriteScreenUiState(
//                athleteId = -1,
//                favoriteEvents = emptyList()
//            ) // Initial value for UI state
//        )

    val athleteId = userPreferencesRepository.athleteLoginId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = -1
        )

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _uiState = MutableStateFlow(EventScreenUiState(
        athleteId = -1,
        eventId = -1, //TODO Delete?
        event = Event(
            title = "",
            startDateString = "-",
            endDateString = "-",
            county = "-",
            country = "-",
            city = "-",
            locationName = "-",
            organizerName = "-",
            description = "-"),
        eventResults = emptyList()
    ))
    val uiState = _uiState.asStateFlow()

    fun updateEventId(id: Int) {
        viewModelScope.launch {
            getEvent(id).collect { event ->
                val eventResults = appRepository.getAllEventResultsNotFlow(eventId = id)
                _uiState.update { currentState ->
                    currentState.copy(
                        eventId = id,
                        event = event,
                        eventResults = eventResults
                    )
                }
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val eventResults = appRepository.getAllEventResultsNotFlow(eventId = event.id)
                currentState.copy(
                    event = event,
                    eventResults = eventResults
                )
            }
        }
    }

    fun getEvent(id: Int): Flow<Event> {
        return appRepository.getEvent(id)
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
                _isFavorite.update {
                    true
                }
            }
        }
        else {
            viewModelScope.launch {
                appRepository.removeFavoriteEvent(
                    athleteId = athleteId,
                    eventId = eventId
                )
                _isFavorite.update {
                    false
                }
            }
        }
    }

    fun checkFavoriteEvent(athleteId: Int, eventId: Int) {
        viewModelScope.launch {
            appRepository.checkFavoriteEvent(athleteId, eventId)
                .collect { count ->
                _isFavorite.update {
                    count > 0
                }
            }
        }
    }

    fun getAllEventResults(eventId: Int): StateFlow<List<Result>> {
        return appRepository.getAllEventResults(eventId).
                stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                EventScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }

}

data class EventScreenUiState(
    val athleteId: Int,
    val eventId: Int, //TODO Delete
    val event: Event,
    val eventResults: List<Result>
)