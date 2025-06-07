package com.example.racebuddy.ui.v2.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.Athlete
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.data.database.EventIdForFavorite
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import network.chaintech.kmp_date_time_picker.utils.now
import kotlin.math.log

class MainScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState(
        athleteInfo = testAthlete,
        events = emptyList(),
        filteredEvents = emptyList(),
        favoriteEventIds = emptyList(),
        selectedFilter = "All",
        isRefreshing = false
    ))
    val uiState = _uiState.asStateFlow()

    val athleteInfo = userPreferencesRepository.supabaseAthleteInfo.map { athleteInfo ->
        athleteInfo
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        testAthlete // or some default UserInfo
    )

    init {
        viewModelScope.launch {

//
            val events = appRepository.getSupabaseEvents()
            Log.d("MAINSCREEN", "Event Request made by viewmodel... - $events")

            val filteredEvents = events.filter { eventInfo ->
                eventInfo.startDate >= LocalDate.now()
            }
//
//
//            // TODO: Instead of getting info from database -> get info from shared preferences
//            val loggedInAthleteId = appRepository.getSupabaseLoggedInAthlete()
//            val loggedInAthleteInfo = appRepository.getSupabaseAthleteInfo(loggedInAthleteId)

            athleteInfo.collect { athleteInfo ->
                val favoriteEventIds = if(athleteInfo.athleteId != "-1") appRepository.getSupabaseFavoriteEventIds(
                    athleteId =  athleteInfo.athleteId ?: ""
                ) else emptyList()

                Log.d("MAINSCREEN", "Favorite events: $favoriteEventIds")

                _uiState.update { currentState ->
                    currentState.copy(
                        events = events,
                        filteredEvents = filteredEvents,
                        athleteInfo = athleteInfo,
                        favoriteEventIds = favoriteEventIds
                    )
                }
            }

            // TODO: Where to put this?
//            val favoriteEventIds = appRepository.getSupabaseFavoriteEventIds(
//                athleteId =  loggedInAthleteInfo.
//            )
        }




//        viewModelScope.launch {
//
//            _uiState.update { currentState ->
//                currentState.copy(
//                    athleteId = appRepository.getSupabaseLoggedInAthlete()
//                )
//            }

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


        //}
    }

//    fun updateAthleteId() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                athleteId = appRepository.getSupabaseLoggedInAthlete()
//            )
//        }
//    }

    fun addFavoriteEventInList(eventUuid: String) {
        Log.d("MAINSCREEN", "adding favorite event to the list...")
        _uiState.update { currentState ->
            val newFavoriteEvents = _uiState.value.favoriteEventIds.toMutableList()
            newFavoriteEvents += EventIdForFavorite(eventUuid)
            Log.d("MAINSREEN", "Updated list: $newFavoriteEvents")
            currentState.copy(
                favoriteEventIds = newFavoriteEvents
            )
        }
    }

    fun deleteFavoriteEventInList(eventUuid: String) {
        _uiState.update { currentState ->
            val newFavoriteEvents = _uiState.value.favoriteEventIds.toMutableList()
            newFavoriteEvents -= EventIdForFavorite(eventUuid)
            Log.d("MAINSREEN", "Updated list: $newFavoriteEvents")
            currentState.copy(
                favoriteEventIds = newFavoriteEvents
            )
        }
    }

    fun onFavoriteIconClick(eventUuid: String, delete: Boolean) {
        viewModelScope.launch {
            if (delete) {
                appRepository.deleteSupabaseFavoriteEvent(_uiState.value.athleteInfo.athleteId ?: "", eventUuid)
                deleteFavoriteEventInList(eventUuid)
            }
            else {
                appRepository.addSupabaseFavoriteEvent(_uiState.value.athleteInfo.athleteId ?: "", eventUuid)
                addFavoriteEventInList(eventUuid)
            }
        }
    }


    fun updateAthlete() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    athleteInfo = appRepository.getSupabaseAthleteInfo(appRepository.getSupabaseLoggedInAthlete())
                )
            }

            getFavoriteEventIds()
        }
    }

    fun isUserLoggedin(): Boolean {
        if(_uiState.value.athleteInfo.athleteId ?: "" != "-1") {
            return true
        }
        return false
    }

    fun getFavoriteEventIds() {
        //TODO: Check how many times the database is interogated
        //TODO: Ideally, the function should be called only once


        // Verifies if there is a user logged in
        if(isUserLoggedin()) {

            Log.d("MAINSCREEN ViewModel", "Getting favorite events for: ${_uiState.value.athleteInfo.athleteId}")

            viewModelScope.launch {
                val favoriteEventIds = appRepository.getSupabaseFavoriteEventIds(
                    _uiState.value.athleteInfo.athleteId ?: ""
                )
                Log.d("MAINSCREENVM", "Favorite events: $favoriteEventIds")

                _uiState.update { currentState ->
                    currentState.copy(
                        favoriteEventIds = favoriteEventIds
                    )
                }
            }
        }
        else {
            Log.d("MAINSCREEN", "Tried getting favorite events -> user it not logged in")
        }
    }

    fun updateFilteredEventsByCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                filteredEvents = if(category != "All") _uiState.value.events.filter { it.category == category && it.startDate >= LocalDate.now ()} else _uiState.value.events.filter { it.startDate >= LocalDate.now () }
            )
        }
    }

    fun updateSelectedFilter(filter: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter
            )
        }
    }

    fun updateEvents(events: List<EventInfo>) {
        _uiState.update { currentState ->
            currentState.copy(
                events = events
            )
        }
    }

    fun updateIsRefreshing(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isRefreshing = value
            )
        }
    }

    fun refreshUi() {
        viewModelScope.launch {
            Log.d("Main Screen", "Is refreshing...")
            updateIsRefreshing(true)
            delay(500)
            val events = appRepository.getSupabaseEvents()

            updateEvents(events)
            delay(500)
            updateIsRefreshing(false)
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
    val athleteInfo: AthleteInfo,
    val events: List<EventInfo>,
    val filteredEvents: List<EventInfo>,
    val favoriteEventIds: List<EventIdForFavorite>,
    val selectedFilter: String,
    val isRefreshing: Boolean,
)