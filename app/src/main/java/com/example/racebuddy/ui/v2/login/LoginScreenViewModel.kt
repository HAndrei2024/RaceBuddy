package com.example.racebuddy.ui.v2.login

import android.provider.ContactsContract
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
import com.example.racebuddy.data.database.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.admin.AdminUserBuilder
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    val appRepository: AppRepository
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
                loginSucces = value
            )
        }
    }

    private fun errorMessageSuccesUpdate(value: Boolean) {
        _uiState.update { currentValue ->
            currentValue.copy(
                errorMessage = true
            )
        }
    }

    fun onLoginButtonClick() {
        viewModelScope.launch {
            val responseString = appRepository.verifySupabaseLogin(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            if(responseString == "true") {
                loginSuccesUpdate(true)
                errorMessageSuccesUpdate(false)
            } else {
                loginSuccesUpdate(false)
                errorMessageSuccesUpdate(true)
                Log.d("LOGIN", responseString)
            }
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
                    appRepository = application.container.appRepository
                )
            }
        }
    }
}



data class LoginScreenUiState(
    val email: String,
    val password: String,
    val errorMessage: Boolean = false,
    val loginSucces: Boolean = false
)