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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProfileScreenViewModel"

class ProfileScreenViewModel(
    private val appRepository: AppRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileScreenUiState(athlete = Athlete(id = -1, name = "", surname = "", username = "", password = ""))
            )
    val uiState = _uiState.asStateFlow()

    private val _athleteLoginId = userPreferencesRepository.athleteLoginId.map { athleteLoginId ->
        athleteLoginId
    }


    init {
        viewModelScope.launch {
            _athleteLoginId.collect { athleteLoginId ->
                if(athleteLoginId > 0) {
                    getAthlete(athleteLoginId)
                }
                else {
                    //TODO
                }
            }
        }
    }

    fun getAthlete(id: Int) {
        Log.d(TAG, "Fetching athlete with id: $id")
        viewModelScope.launch {
            appRepository.getAthlete(id)
                .collect { athlete ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            athlete = athlete
                        )
                    }
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
    val athlete: Athlete
)