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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        )
    )
    val uiState = _uiState.asStateFlow()



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
    val selectedFilter: String
)