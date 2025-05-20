package com.example.racebuddy.ui.v2.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

class SignupScreensViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupScreensUiState())
    val uiState = _uiState.asStateFlow()

    fun onFirstNameChange(firstName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                firstName = firstName
            )
        }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                lastName = lastName
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email
            )
        }

    }

    fun updateSignUpSucces(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                signupSucces = value
            )
        }
    }

    fun updateShowError(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showError = value
            )
        }
    }

    fun updateErrorMessage(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = message
            )
        }
    }

    private fun validatePassword(password: String): Boolean {
        val conditions = listOf(
            "At least 8 characters" to (password.length >= 8),
            "At least one special character" to password.any { it in "!@#\$%^&*()_-+=<>?/\\[]{}|~`" },
            "At least one digit" to password.any { it.isDigit() },
            "At least one uppercase letter" to password.any { it.isUpperCase() }
        )

        conditions.forEach{ (_, isTrue) ->
            if(!isTrue) {
                return false
            }
        }

        return true
    }

    fun signUp() {
        updateIsLoading(true)

        if(validatePassword(_uiState.value.password)) {
            viewModelScope.launch {
                val responeString = appRepository.signUpSupabase(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                Log.d("SIGNUP1", responeString)

                if (responeString.substring(0, 4) == "true") {
                    updateSignUpSucces(true)
                    updateShowError(false)

                    // Update user preferences with user id
                    // Create a new row in the Athlete Table in supabase (or let supabase handle it)

                    userPreferencesRepository.saveSupabaseAthleteId(responeString.substring(6))
                    Log.d("SINGUP1", responeString.substring(6))
                } else {
                    updateSignUpSucces(false)
                    updateShowError(true)
                    updateErrorMessage(responeString)
                }
                updateIsLoading(false)
            }
        }
        else if (_uiState.value.email.isEmpty()){
            updateSignUpSucces(false)
            updateShowError(true)
            updateErrorMessage("No email provided.")
            updateIsLoading(false)
        }
        else {
            updateSignUpSucces(false)
            updateShowError(true)
            updateErrorMessage("Password doesn't meet requirments.")
            updateIsLoading(false)
        }
    }

    fun onDoneClick() {

    }

    fun updateIsLoading(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = value
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

    fun onVerifyPasswordChange(verifyPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                verifyPassword = verifyPassword
            )
        }
    }

    fun onContinueButtonClick() {
        _uiState.update { currentState ->
            currentState.copy(
                currentSignUpScreen = CurrentSignUpScreen.SignupScreenSecond
            )
        }

        // Verify if passwords match and sign up
        if(_uiState.value.password != _uiState.value.verifyPassword) {
            updateShowError(true)
            updateErrorMessage("Passwords don't match!")
        }
        else {
            updateShowError(false)
            signUp()
        }
    }

    fun onBirthdateTextfieldClick() {
        _uiState.update { currentState ->
            currentState.copy(
                showDatePicker = true
            )
        }
    }

    fun setShowDatePickerFalse() {
        _uiState.update { currentState ->
            currentState.copy(
                showDatePicker = false
            )
        }
    }

    fun onDatePickerDoneClick() {
        _uiState.update { currentState ->
            currentState.copy(
                showDatePicker = false
            )
        }
        Log.d("SIGNUP2", "Pressed Done on DatePicker")
    }


    fun onDatePickerDissmisClick() {
        setShowDatePickerFalse()
    }

    fun onNationalityChange(nationality: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nationality = nationality
            )
        }
    }

    fun onDayChange(day: String) {
        _uiState.update { currentState ->
            currentState.copy(
                day = day
            )
        }
    }

    fun onMonthChange(month: String) {
        _uiState.update { currentState ->
            currentState.copy(
                month = month
            )
        }
    }

    fun onYearChange(year: String) {
        _uiState.update { currentState ->
            currentState.copy(
                year = year
            )
        }
    }

    fun onGenderChange(gender: String) {
        _uiState.update { currentState ->
            currentState.copy(
                gender = gender
            )
        }
    }

    fun onLicenseNumberChange(localRegistrationNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(
                localRegistrationNumber = localRegistrationNumber
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                SignupScreensViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

enum class CurrentSignUpScreen(val index: Int) {
    SignupScreenFirst(0),
    SignupScreenSecond(1)
}

data class SignupScreensUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val verifyPassword: String = "",
    val nationality: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val gender: String = "",
    val localRegistrationNumber: String = "",
    val showError: Boolean = false,
    val errorMessage: String = "",
    val signupSucces: Boolean = false,
    val isLoading: Boolean = false,
    val showDatePicker: Boolean = false,
    // Remove or not?
    val currentSignUpScreen: CurrentSignUpScreen = CurrentSignUpScreen.SignupScreenFirst
)