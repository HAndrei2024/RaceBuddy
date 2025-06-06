package com.example.racebuddy.ui.v2.profile

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventIdForFavorite
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.EventResultProfileInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.v2.main.MainScreenUiState
import com.example.racebuddy.ui.v2.main.MainScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        ProfileScreenUiState(
            athleteInfo = testAthlete,
            eventResultProfileInfoList = emptyList(),
            registeredEventsUuid = emptyList(),
            filteredEventResultProfileInfoList = emptyList(),
            selectedFilterButton = "Results",
            selectedFilterResultButton = "All"
        )
    )
    val uiState = _uiState.asStateFlow()

    fun getEventResultProfileInfoList(athleteUuid: String) {
        viewModelScope.launch {
            val list = appRepository.getSupabaseEventResultProfileInfo(athleteUuid)

            updateEventResultProfileList(list)
            updateFilteredEventResultProfileList(list)
        }
    }

    fun getRegisteredEventsUuids(athleteUuid: String) {
        viewModelScope.launch {
            val list = appRepository.getSupabaseAthleteRegisteredEventsUuid(athleteUuid)

            updateRegisteredEventsUuidsList(list)
        }
    }

    fun updateEventResultProfileList(list: List<EventResultProfileInfo>) {
        Log.d("PROFILE VM", "updating results list... ${list.size}")
        _uiState.update { currentValue ->
            currentValue.copy(
                eventResultProfileInfoList = list
            )
        }
    }

    fun updateFilteredEventResultProfileList(list: List<EventResultProfileInfo>) {
        Log.d("PROFILE VM", "updating filtered results list... ${list.size}")
        _uiState.update { currentValue ->
            currentValue.copy(
                filteredEventResultProfileInfoList = list
            )
        }
    }


    fun updateRegisteredEventsUuidsList(list: List<String>) {
        _uiState.update { currentValue ->
            currentValue.copy(
                registeredEventsUuid = list
            )
        }
    }

    fun updateSelectedFilterResultButton(filter: String) {
        _uiState.update { currentValue ->
            currentValue.copy(
                selectedFilterResultButton = filter
            )
        }
    }

    fun updateSelectedFilterButton(filter: String) {
        _uiState.update { currentValue ->
            currentValue.copy(
                selectedFilterButton = filter
            )
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            appRepository.logoutSupabaseAthlete()
            userPreferencesRepository.logoutSupabaseAthleteInfo()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                ProfileScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class ProfileScreenUiState(
    val athleteInfo: AthleteInfo,
    val eventResultProfileInfoList: List<EventResultProfileInfo>,
    val filteredEventResultProfileInfoList: List<EventResultProfileInfo>,
    val registeredEventsUuid: List<String>,
    val selectedFilterButton: String,
    val selectedFilterResultButton: String,
)
