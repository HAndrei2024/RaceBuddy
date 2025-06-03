package com.example.racebuddy.ui.v2.event

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
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.junit.experimental.categories.Category
import kotlin.uuid.Uuid

class EventScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventScreenUiState(
        eventInfo = testEvent,
        resultAthleteInfoList = emptyList(),
        resultAthleteInfoListFiltered = emptyList()
    ))
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

    fun getListOfCategories(): List<String> {
        return listOf("General") + _uiState.value.resultAthleteInfoList.map { it -> it.category }.distinct()
    }

    fun updateFilterResultsOnCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                resultAthleteInfoListFiltered =
                if(category != "General") _uiState.value.resultAthleteInfoList.filter { it.category == category }
                else _uiState.value.resultAthleteInfoList.sortedBy { it.time }
            )
        }
    }

    fun onRegisterButtonClick(athleteUuid: String, eventUuid: String, category: String) {
        viewModelScope.launch {
            appRepository.registerSupabaseAthleteToAnEvent(
                athleteUuid = athleteUuid,
                eventUuid = eventUuid,
                category = category
            )
        }
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
    val eventInfo: EventInfo,
    val resultAthleteInfoList: List<ResultAthleteInfo>,
    val resultAthleteInfoListFiltered: List<ResultAthleteInfo>
)