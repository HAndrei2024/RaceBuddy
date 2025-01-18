package com.example.racebuddy.ui.profile

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
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.favorite.FavoriteScreenUiState
import com.example.racebuddy.ui.favorite.FavoriteScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProfileScreenViewModel"

class ProfileScreenViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = userPreferencesRepository.athleteLoginId
        .flatMapLatest { athleteLoginId ->
            if(athleteLoginId > 0) {
                appRepository.getAthlete(athleteLoginId).map { athlete ->
                    ProfileScreenUiState(
                        athlete = athlete
                    )
                }
            }
            else {
                flowOf(ProfileScreenUiState(
                    athlete = Athlete(id = -1, name = "", surname = "", username = "", password = "")
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileScreenUiState(
                athlete = Athlete(id = -1, name = "", surname = "", username = "", password = "")
            )
        )


    fun onLogoutButtonClick() {
        viewModelScope.launch {
            userPreferencesRepository.saveAthleteLoginId(-1)
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
    val athlete: Athlete
)