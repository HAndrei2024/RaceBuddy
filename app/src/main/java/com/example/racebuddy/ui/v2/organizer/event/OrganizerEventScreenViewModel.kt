package com.example.racebuddy.ui.v2.organizer.event

import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.EventInfoWithNumberOfParticipants
import com.example.racebuddy.data.database.ResultAthleteInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.data.network.LocalServerApi
import com.example.racebuddy.ui.v2.event.EventScreenUiState
import com.example.racebuddy.ui.v2.event.EventScreenViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class OrganizerEventScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        OrganizerEventScreenUiState(
        eventInfo = testEvent,
        resultAthleteInfoList = emptyList(),
        resultAthleteInfoListFiltered = emptyList(),
        similarEvents = emptyList(),
        predictedValue = -1
        )
    )
    val uiState = _uiState.asStateFlow()

    fun updateEvent(eventInfo: EventInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                eventInfo = eventInfo,
                predictedValue = -1
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

    fun onFilterCategoryButtonClick(category: String) {
        if(category == "General") {
            updateResutAthleteInfoListFiltered(_uiState.value.resultAthleteInfoList.sortedBy { it.firstName })
        }
        else {
            updateResutAthleteInfoListFiltered(_uiState.value.resultAthleteInfoList.filter { it.category == category }.sortedBy { it.firstName })
        }
    }

    fun onConfirmRegistrationButtonPressed(athleteUuid: String, eventUuid: String, value: Boolean) {
        viewModelScope.launch {
            appRepository.updateSupabaseConfirmedFieldForRegisteredAthlete(
                athleteUuid = athleteUuid,
                eventUuid = eventUuid,
                value = value
            )

           updateConfirmForAthlete(athleteUuid, value)
        }
    }

    fun updateConfirmForAthlete(athleteUuid: String, value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                resultAthleteInfoList = _uiState.value.resultAthleteInfoList.map { if(it.athleteUuid == athleteUuid) it.copy(confirmed = value) else it },
                resultAthleteInfoListFiltered = _uiState.value.resultAthleteInfoListFiltered.map { if(it.athleteUuid == athleteUuid) it.copy(confirmed = value) else it }
            )
        }
    }

    fun getSimilarEvents() {
        viewModelScope.launch {
            val similarEvents = appRepository.getEventsInfoWithNumberOfParticipants(
                eventUuid = _uiState.value.eventInfo.eventUuid,
                category = _uiState.value.eventInfo.category,
                country = _uiState.value.eventInfo.country
            )

            updateSimilarEvents(similarEvents)
        }
    }

    fun updateSimilarEvents(similarEvents: List<EventInfoWithNumberOfParticipants>) {
        _uiState.update { currentState ->
            currentState.copy(
                similarEvents = similarEvents
            )
        }
    }

    fun updatePredictedValue(value: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                predictedValue = value
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

    fun getNumberOfMembersPrediction() {
        viewModelScope.launch {
            try {
                updateIsLoading(true)
                val event = mapOf(
                    "start_date" to _uiState.value.eventInfo.startDate.toString(),
                    "end_date" to _uiState.value.eventInfo.endDate.toString(),
                    "country" to _uiState.value.eventInfo.country,
                    "category" to _uiState.value.eventInfo.category
                )


                val eventJson = Gson().toJson(event)
                val eventPart = eventJson.toRequestBody("text/plain".toMediaType())

                val result = LocalServerApi.retrofitService.getPrediction(
                    event = eventPart
                )

                updatePredictedValue(result.body()?.predictedParticipants ?: -1)

                updateIsLoading(false)
                Log.d("OrganizerEvent", "Prediction successful: $result and body of result: ${result.body()?.predictedParticipants}")
            }
            catch (e: Exception) {
                updateIsLoading(false)
                Log.d("OrganizerEvent", "Prediction error: $e")
            }
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
    val similarEvents: List<EventInfoWithNumberOfParticipants>,
    val predictedValue: Int,
    val isLoading: Boolean = false
)