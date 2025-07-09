package com.example.racebuddy.ui.v2.organizer.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.ResultAthleteInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.ui.v2.event.EventScreenUiState
import com.example.racebuddy.ui.v2.event.EventScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganizerEventScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        OrganizerEventScreenUiState(
        eventInfo = testEvent,
        resultAthleteInfoList = emptyList(),
        resultAthleteInfoListFiltered = emptyList()
    )
    )
    val uiState = _uiState.asStateFlow()

    fun updateEvent(eventInfo: EventInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                eventInfo = eventInfo
            )
        }
    }

    fun getEventResultAthleteInfo(eventUuid: String) {
        viewModelScope.launch {
            val list = appRepository.getSupabaseEventResultAthleteInfo(eventUuid)
            Log.d("EventScreenVM", " Trying to update resultAthlteInfo List... $list")
            updateResutAthleteInfoList(resultAthleteInfoList = list)
            updateResutAthleteInfoListFiltered(resultAthleteInfoList = list)
        }
    }

    fun updateResutAthleteInfoList(resultAthleteInfoList: List<ResultAthleteInfo>) {
        _uiState.update { currentState ->
            currentState.copy(
                resultAthleteInfoList = resultAthleteInfoList
            )
        }
    }

    fun updateResutAthleteInfoListFiltered(resultAthleteInfoList: List<ResultAthleteInfo>) {
        _uiState.update { currentState ->
            currentState.copy(
                resultAthleteInfoListFiltered = resultAthleteInfoList
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                OrganizerEventScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class OrganizerEventScreenUiState(
    val eventInfo: EventInfo,
    val resultAthleteInfoList: List<ResultAthleteInfo>,
    val resultAthleteInfoListFiltered: List<ResultAthleteInfo>,
    val isLoading: Boolean = false
)