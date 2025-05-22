package com.example.racebuddy.ui.v2.login

import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.UserPreferencesRepository
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.admin.AdminUserBuilder
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class LoginScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState("", ""))
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }

    private fun loginSuccesUpdate(value: Boolean) {
        _uiState.update { currentValue ->
            currentValue.copy(
                isLoading = false,
                loginSucces = value
            )
        }
    }

    fun isLoadingUpdate(value: Boolean) {
        _uiState.update { currentValue ->
            currentValue.copy(
                isLoading = value
            )
        }
    }

    private fun errorMessageSuccesUpdate(value: Boolean) {
        _uiState.update { currentValue ->
            currentValue.copy(
                errorMessage = value
            )
        }
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onLoginButtonClick() {
        errorMessageSuccesUpdate(false)
        isLoadingUpdate(true)
        Log.d("LOGIN", "Loading set to true.")

        if(checkEmail(_uiState.value.email) && _uiState.value.password.isNotEmpty()) {
            viewModelScope.launch {
                Log.d("LOGIN", "API CALL")
                val responseString = appRepository.verifySupabaseLogin(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )

                if (responseString.substring(0, 4) == "true") {
                    loginSuccesUpdate(true)
                    errorMessageSuccesUpdate(false)

                    // Update user preferences (id)

                    //userPreferencesRepository.saveSupabaseAthleteId(responseString.substring(6))
                    Log.d("LOGIN", "Current logged in supabase user: " + appRepository.getSupabaseLoggedInAthlete())
                } else {
                    loginSuccesUpdate(false)
                    errorMessageSuccesUpdate(true)
                    Log.d("LOGIN", responseString)
                }

                isLoadingUpdate(false)
            }
        }
        else {
            loginSuccesUpdate(false)
            errorMessageSuccesUpdate(true)
        }
    }

    fun onSignUpTextClick() {

    }

    fun onSkipButtonClick() {

    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                LoginScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}



data class LoginScreenUiState(
    val email: String,
    val password: String,
    val errorMessage: Boolean = false,
    val isLoading: Boolean = false,
    val loginSucces: Boolean = false
)