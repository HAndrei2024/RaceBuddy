package com.example.racebuddy.ui.v2.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

class SignupScreensViewModel(
    val appRepository: AppRepository
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

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }

    fun onContinueButtonClick() {
        _uiState.update { currentState ->
            currentState.copy(
                currentSignUpScreen = CurrentSignUpScreen.SignupScreenSecond
            )
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
                    appRepository = application.container.appRepository
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
    val nationality: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val gender: String = "",
    val localRegistrationNumber: String = "",
    val errorMessage: Boolean = false,
    val signupSucces: Boolean = false,
    val showDatePicker: Boolean = false,
    // Remove or not?
    val currentSignUpScreen: CurrentSignUpScreen = CurrentSignUpScreen.SignupScreenFirst
)