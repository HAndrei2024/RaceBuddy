package com.example.racebuddy.ui.v2.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.BuildConfig
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventIdForFavorite
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.EventResultProfileInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.data.network.StravaApi
import com.example.racebuddy.ui.v2.main.MainScreenUiState
import com.example.racebuddy.ui.v2.main.MainScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val TAG = "Profile Screen VM"

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
            selectedFilterResultButton = "All",
            stravaResponseCode = "null"
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
        Log.d(TAG, "updating results list... ${list.size}")
        _uiState.update { currentValue ->
            currentValue.copy(
                eventResultProfileInfoList = list
            )
        }
    }

    fun updateFilteredEventResultProfileList(list: List<EventResultProfileInfo>) {
        Log.d(TAG, "updating filtered results list... ${list.size}")
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

    fun onUpdateProfilePicFromStravaClick(context: Context) {
        viewModelScope.launch {
            val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
                .appendQueryParameter("redirect_uri", BuildConfig.REDIRECT_URL)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("approval_prompt", "auto")
                .appendQueryParameter("scope", "activity:write,read")
                .build()

            val intentFromButton = Intent(Intent.ACTION_VIEW, intentUri)

            context.startActivity(intentFromButton)
        }
    }

    fun updateResponseCode(response: String, athleteUuid: String, updateLocalAthleteInfo: (profilePicUrl: String) -> Unit) {
        if(response != "null") {
            if(response != _uiState.value.stravaResponseCode) {
                _uiState.update { currentState ->
                    currentState.copy(
                        stravaResponseCode = response
                    )
                }
                updateProfilePictureFromProfileScreen(
                    responseCode = response,
                    athleteUuid = athleteUuid,
                    updateLocalAthleteInfo = updateLocalAthleteInfo
                )
            }
        }
    }

    private fun updateProfilePictureFromProfileScreen(responseCode: String, athleteUuid: String, updateLocalAthleteInfo: (profilePicUrl: String) -> Unit) {
        viewModelScope.launch {
            Log.d(TAG, "Making request to Strava")
            val stravaAuthResult = StravaApi.retrofitService.getAuthDetails(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                authorizationCode = responseCode
            )
            if (!stravaAuthResult.athlete.profilePictureUrl.contains("avatar/athlete")) {
                // Database update
                Log.d(TAG, "Updating database: ${_uiState.value.athleteInfo.athleteId}, ${stravaAuthResult.athlete.profilePictureUrl}")
                appRepository.updateSupabaseAthleteProfilePic(
                    athleteUuid = athleteUuid,
                    profilePictureUrl = stravaAuthResult.athlete.profilePictureUrl
                )

                updateLocalAthleteInfo(stravaAuthResult.athlete.profilePictureUrl)
            }
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
    val stravaResponseCode: String
)
