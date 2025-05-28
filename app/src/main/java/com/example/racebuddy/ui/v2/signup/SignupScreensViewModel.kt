package com.example.racebuddy.ui.v2.signup

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
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
import com.example.racebuddy.ui.v2.login.LoginScreenUiState
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
//import java.time.format.DateTimeFormatter

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

                    // TODO: This line of code causes recomposition => signup second screen composes twice
                    //userPreferencesRepository.saveSupabaseAthleteId(responeString.substring(6))

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

    private fun formateDate(): String {
        val input = "${_uiState.value.day}-${_uiState.value.month}-${_uiState.value.year}"

        val localDate = LocalDate(_uiState.value.year.toInt(), _uiState.value.month.toInt(), _uiState.value.day.toInt())
        val formattedDate = localDate.toString()

        return formattedDate
    }

    fun onDoneClick() {
        updateShowError(false)
        validateSecondScreenTextFields()

        updateIsLoading(true)
        // Get signed in user id from Supabase/Shared Preferences
        val athleteId = appRepository.getSupabaseLoggedInAthlete()

        if(athleteId.isNotEmpty()) {
            // Update the Athlete table

            if(validateSecondScreenTextFields()) {
                viewModelScope.launch {
                    val athleteInfo: AthleteInfo = AthleteInfo(
                        createdAt = null,
                        firstName = _uiState.value.firstName,
                        lastName = _uiState.value.lastName,
                        birthdate = formateDate(),
                        gender = _uiState.value.gender,
                        country = _uiState.value.nationality,
                        phoneNumber = null,
                        username = null,
                        athleteId = athleteId,
                        licenseNumber = _uiState.value.localRegistrationNumber,
                        uciLicenseNumber = null,
                        profilePictureUrl = ""
                    )

                    updateUpdatedDatabase(appRepository.updateSupabaseAthleteDetails(athleteInfo))

                    if(!_uiState.value.updatedDatabase) {
                        updateShowError(true)
                        updateErrorMessage("Something went wrong.")
                    }
                    else {
                        updateShowError(false)
                    }
                }
            }
        }
        else {
            updateShowError(true)
            updateErrorMessage("Something went wrong. Press Skip.")
        }
    }

    fun updateUpdatedDatabase(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                updatedDatabase = value
            )
        }
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
//        _uiState.update { currentState ->
//            currentState.copy(
//                currentSignUpScreen = CurrentSignUpScreen.SignupScreenSecond
//            )
//        }

        // Verify if passwords match and sign up
        if(_uiState.value.password != _uiState.value.verifyPassword) {
            updateShowError(true)
            updateErrorMessage("Passwords don't match!")
        }
        else {
            updateShowError(false)

//            _uiState.update { currentState ->
//                currentState.copy(
//                    password = _uiState.value.password.trim()
//                )
//            }

            signUp()
        }
    }

    fun validateSecondScreenTextFields(): Boolean {
        if(
            !(isValidInternationalName(_uiState.value.firstName) &&
            isValidInternationalName(_uiState.value.lastName) &&
            isValidLicenseNumber() &&
            _uiState.value.gender.isNotEmpty() &&
            _uiState.value.nationality.isNotEmpty()
            )
        ) {
            updateShowError(true)
            updateErrorMessage("Some fields are completed improperly.")
            return false
        }
        else {
            updateShowError(false)
            return true
        }
    }

    fun isValidLicenseNumber(): Boolean {
        if (_uiState.value.localRegistrationNumber.matches(Regex("^[A-Za-z0-9]{4,50}$")) == true || _uiState.value.localRegistrationNumber.isEmpty()){
            return true
        }
        return false
    }

    fun isValidInternationalName(name: String): Boolean {
        val cleaned = name.trim()
        return cleaned.matches(Regex("^[\\p{L} ,.'-]{2,50}$"))
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
    val updatedDatabase: Boolean = false,
    // Remove or not?
    val currentSignUpScreen: CurrentSignUpScreen = CurrentSignUpScreen.SignupScreenFirst,
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)