package com.example.racebuddy.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.BuildConfig
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.Athlete
import com.example.racebuddy.data.database.Result
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.network.StravaApi
import com.example.racebuddy.data.network.StravaAthlete
import com.example.racebuddy.data.network.StravaAuthResponse
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

    private val _responseCode = MutableStateFlow<String>("null")
    val responseCode = _responseCode.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = userPreferencesRepository.athleteLoginId
        .flatMapLatest { athleteLoginId ->
            if(athleteLoginId > 0) {
                appRepository.getAthlete(athleteLoginId).map { athlete ->
                    val resultsList = appRepository.getAllAthleteResults(
                        athleteId = athleteLoginId
                    )
                    ProfileScreenUiState(
                        athlete = athlete,
                        athleteResults = resultsList
                    )
                }
            }
            else {
                flowOf(ProfileScreenUiState(
                    athlete = Athlete(id = -1, name = "", surname = "", username = "", password = ""),
                    athleteResults = emptyList()
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileScreenUiState(
                athlete = Athlete(id = -1, name = "", surname = "", username = "", password = ""),
                athleteResults = emptyList()
            )
        )


    fun onLogoutButtonClick() {
        viewModelScope.launch {
            userPreferencesRepository.saveAthleteLoginId(-1)
        }
    }

    fun onGetInfoFromStravaClick(context: Context) {
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

    fun updateResponseCode(response: String) {
        if(response != "null") {
            if(response != responseCode.value) {
                _responseCode.update {
                    response
                }
                updateProfilePictureFromProfileScreen(
                    responseCode = response
                )
            }
        }
    }

    private fun updateProfilePictureFromProfileScreen(responseCode: String) {
        viewModelScope.launch {
            Log.d(TAG, "Making request to Strava")
            val stravaAuthResult = StravaApi.retrofitService.getAuthDetails(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                authorizationCode = responseCode
            )
            if (!stravaAuthResult.athlete.profilePictureUrl.contains("avatar/athlete")) {
                appRepository.updateAthleteProfilePicture(
                    profilePictureUrl = stravaAuthResult.athlete.profilePictureUrl,
                    id = uiState.value.athlete.id
                )
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
    val athlete: Athlete,
    val athleteResults: List<Result>,
)