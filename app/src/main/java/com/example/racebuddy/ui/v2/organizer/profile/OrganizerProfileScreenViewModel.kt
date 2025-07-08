package com.example.racebuddy.ui.v2.organizer.profile

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
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.RemoteDataSource
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.ui.v2.organizer.main.OrganizerMainScreenUiState
import com.example.racebuddy.ui.v2.organizer.main.OrganizerMainScreenViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganizerProfileScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrganizerProfileScreenUiState(
            organizerInfo = defaultOrganizer,
            isRefreshing = false
        )
    )
    val uiState = _uiState.asStateFlow()

    fun updateOrganizer() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    organizerInfo = appRepository.getSupabaseOrganizerInfo(appRepository.getSupabaseLoggedInAthlete())

                )
            }
        }
    }

    fun refreshUi() {
        viewModelScope.launch {
            Log.d("Organizer Profile Screen", "Is refreshing...")
            updateIsRefreshing(true)
            delay(500)

            updateOrganizer()

            delay(500)
            updateIsRefreshing(false)
        }
    }

    fun updateIsRefreshing(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isRefreshing = value
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                OrganizerProfileScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class OrganizerProfileScreenUiState(
    val organizerInfo: OrganizerInfo,
    val isRefreshing: Boolean
)