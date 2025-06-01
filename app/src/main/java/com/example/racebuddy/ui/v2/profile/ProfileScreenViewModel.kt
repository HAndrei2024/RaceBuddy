package com.example.racebuddy.ui.v2.profile

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
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.main.MainScreenViewModel
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {


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
//
//data class ProfileScreenUiState(
//
//)
