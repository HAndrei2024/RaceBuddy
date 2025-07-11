package com.example.racebuddy.ui.v2.organizer.main

import android.util.Log
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
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.v2.main.MainScreenUiState
import com.example.racebuddy.ui.v2.main.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

class OrganizerMainScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrganizerMainScreenUiState(
            organizerInfo = defaultOrganizer,
            events = emptyList(),
            filteredEvents = emptyList(),
            selectedFilter = "Upcoming",
            isRefreshing = false
        )
    )
    val uiState = _uiState.asStateFlow()

    val organizerInfo = userPreferencesRepository.supabaseOrganizerInfo.map { organizerInfo ->
        organizerInfo
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        defaultOrganizer // or some default UserInfo
    )

    fun updateEvents(organizerUuid: String = organizerInfo.value.organizerUuid) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val events = appRepository.getSupabaseOrganizerEvents(organizerUuid = organizerUuid)

                currentState.copy(
                    events = events,
                    filteredEvents = events.filter { if(_uiState.value.selectedFilter == "Upcoming") it.startDate > LocalDate.now() else it.startDate <= LocalDate.now() }
                )
            }
        }
    }

    fun updateSelectedFilter(filter: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedFilter = filter
                )
            }
        }
    }

    fun updateStateAfterSignUp() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val organizerInfo = appRepository.getSupabaseOrganizerInfo(appRepository.getSupabaseLoggedInAthlete())
                val events = appRepository.getSupabaseOrganizerEvents(organizerInfo.organizerUuid)

                val filteredEvents = events.filter { it.startDate > LocalDate.now() }

                currentState.copy(
                    organizerInfo = appRepository.getSupabaseOrganizerInfo(appRepository.getSupabaseLoggedInAthlete()),
                    events = events,
                    filteredEvents = filteredEvents
                )
            }
        }
    }


    fun updateStateAfterLogin(
        organizerInfo: OrganizerInfo
    ) {
        viewModelScope.launch {
            _uiState.update { currentState ->

                Log.d("OrganizerMainScreenViewModel", "Trying to update state after login... ${organizerInfo.organizerUuid}")
                val events = appRepository.getSupabaseOrganizerEvents(organizerInfo.organizerUuid)
                val filteredEvents = events.filter { it.startDate > LocalDate.now() }


                currentState.copy(
                    organizerInfo = organizerInfo,
                    events = events,
                    filteredEvents = filteredEvents
                )
            }
        }
    }

    fun refreshUi(organizerUuid: String) {
        viewModelScope.launch {
            Log.d("Organizer Main Screen", "Is refreshing...")
            updateIsRefreshing(true)
            delay(500)

            updateEvents(organizerUuid)

            delay(500)
            updateIsRefreshing(false)
        }
    }

    fun reloadUi(organizerUuid: String) {
        viewModelScope.launch {
            Log.d("Organizer Main Screen", "Is refreshing...")
            updateIsLoading(true)
            delay(500)

            updateEvents(organizerUuid)

            delay(500)
            updateIsLoading(false)
        }
    }

    fun updateIsRefreshing(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isRefreshing = value
            )
        }
    }

    fun updateIsLoading(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = value
            )
        }
    }


    fun onFilterButtonClick(filter: String) {
        when(filter) {
            "Finished" -> {
                updateSelectedFilter(filter)
                updateFilteredEventsList(_uiState.value.events.filter { it.endDate < LocalDate.now() })
            }
            "Upcoming" -> {
                updateSelectedFilter(filter)
                updateFilteredEventsList(_uiState.value.events.filter { it.startDate >= LocalDate.now() })
            }
        }
    }

    fun updateFilteredEventsList(filteredEvents: List<EventInfo>) {
        _uiState.update { currentState ->
            currentState.copy(
                filteredEvents = filteredEvents
            )
        }
    }

    fun onRefresh() {

    }

    fun onLogoutClick() {
        viewModelScope.launch {
            appRepository.logoutSupabaseAthlete()
            userPreferencesRepository.logoutSupabaseOrganizerInfo()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                OrganizerMainScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class OrganizerMainScreenUiState(
    val organizerInfo: OrganizerInfo,
    val events: List<EventInfo>,
    val filteredEvents: List<EventInfo>,
    val selectedFilter: String = "Upcoming",
    val isRefreshing: Boolean,
    val isLoading: Boolean = false,
    val detailsFilled: Boolean = false,
)