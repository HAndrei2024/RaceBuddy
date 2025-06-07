package com.example.racebuddy.app.v2

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
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppScreenUiState(athleteInfo = testAthlete))
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
            Log.d("App View Model", "Getting list of events...")

            val list = appRepository.getSupabaseEvents()
            Log.d("App View Model", "Recieved list of events: $list")
            updateListOfEvents(events = list)
        }
    }

    fun updateListOfEvents(events: List<EventInfo>) {
        _uiState.update { currentValue ->
            currentValue.copy(
                events = events
            )
        }
    }

    fun updateUserPreferencesRepository() {
        Log.d("App", "Updating User Preferences...")
        viewModelScope.launch {
            val athleteInfo =
                appRepository.getSupabaseAthleteInfo(appRepository.getSupabaseLoggedInAthlete())
            userPreferencesRepository.saveSupabaseAthleteInfo(
                athleteInfo = athleteInfo
            )

            Log.d("App", "The User Preferences has been updated with: ${athleteInfo.firstName}")
        }
    }

    fun updateUserPreferencesRepository(athleteInfo: AthleteInfo) {
        viewModelScope.launch {
            userPreferencesRepository.saveSupabaseAthleteInfo(
                athleteInfo = athleteInfo
            )
        }
    }

    fun updateAthleteInfoProfilePicUrl(profilePictureUrl: String) {

        val newAthlete = athleteInfo.value.copy(profilePictureUrl = profilePictureUrl)

        updateUserPreferencesRepository(athleteInfo = newAthlete)
//        _uiState.update { currentState ->
//
//            currentState.copy(
//                athleteInfo = newAthlete
//            )
//        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                AppScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class AppScreenUiState(
    val athleteInfo: AthleteInfo,
    val events: List<EventInfo> = emptyList()
)