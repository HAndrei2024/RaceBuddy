package com.example.racebuddy.ui.signup

import android.database.sqlite.SQLiteConstraintException
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
import com.example.racebuddy.ui.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

private const val SIGN_UP_TAG = "SIGN_UP"

class SignUpViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState("", "", "", ""))
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name
            )
        }
    }
    fun onSurnameChange(surname: String) {
        _uiState.update { currentState ->
            currentState.copy(
                surname = surname
            )
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = username
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

    fun updateSignUpSuccess(boolean: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                signUpSuccess = boolean
            )
        }
    }

    fun updateErrorFlagAndMessage(boolean: Boolean, error: String = "") {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessageFlag = boolean,
                errorMessage = error
            )
        }
    }

    fun onSignUpButtonPressed() {
        viewModelScope.launch {
            try {
                appRepository.createNewAccount(
                    Athlete(
                        name = uiState.value.name,
                        surname = uiState.value.surname,
                        username = uiState.value.username,
                        password = uiState.value.password
                    )
                )
                updateSignUpSuccess(true)
                updateErrorFlagAndMessage(false)
            } catch (e: SQLiteConstraintException) {
                Log.e(SIGN_UP_TAG, "Probably the username?", e)
                updateErrorFlagAndMessage(true, "Username already exists!")
                onUsernameChange("-")
            } catch (e: IOException) {
                Log.e(SIGN_UP_TAG, "Probably the database?", e)
                updateErrorFlagAndMessage(true, "Internal Problem!")
                onNameChange("Problems with the database")
            } catch (e: Exception) {
                Log.e(SIGN_UP_TAG, "Other exception", e)
                updateErrorFlagAndMessage(true, e.toString())
                onNameChange("Some other problems")
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                SignUpViewModel(
                    appRepository = application.container.appRepository
                )
            }
        }
    }
}

data class SignUpUiState(
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
    val errorMessageFlag: Boolean = false,
    val errorMessage: String = "",
    val signUpSuccess: Boolean = false
)