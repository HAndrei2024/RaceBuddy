package com.example.racebuddy.ui.v2.organizer.updateDetails

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
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.ui.v2.organizer.main.OrganizerMainScreenUiState
import com.example.racebuddy.ui.v2.organizer.main.OrganizerMainScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganizerUpdateDetailsScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrganizerUpdateDetailsUiState(
            name = "",
            administratorFirstName = "",
            administratorLastName = "",
            country = "",
            identificationNumber = "",
            showError = false,
            errorMessage = "",
        )
    )
    val uiState = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = uiState
        .map { state ->
            listOf(
                state.name,
                state.administratorFirstName,
                state.administratorLastName,
                state.country,
                state.identificationNumber
            ).all { it != "-" }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun initializeFields(
        organizerInfo: OrganizerInfo
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                name = organizerInfo.name,
                administratorFirstName = organizerInfo.administratorFirstName,
                administratorLastName = organizerInfo.administratorLastName,
                country = organizerInfo.country,
                identificationNumber = organizerInfo.identificationNumber,
                showError = false,
                errorMessage = "",
                isUpdateSuccessful = false
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    fun onAdministratorFirstNameChange(firstName: String) {
        _uiState.update { currentState ->
            currentState.copy(administratorFirstName = firstName)
        }
    }

    fun onAdministratorLastNameChange(lastName: String) {
        _uiState.update { currentState ->
            currentState.copy(administratorLastName = lastName)
        }
    }

    fun onCountryChange(country: String) {
        _uiState.update { currentState ->
            currentState.copy(country = country)
        }
    }

    fun onIdentificationNumberChange(idNumber: String) {
        _uiState.update { currentState ->
            currentState.copy(identificationNumber = idNumber)
        }
    }


    fun updateShowError(message: String) {
        _uiState.update { currentState ->
            currentState.copy(showError = true, errorMessage = message)
        }
    }

    fun updateError(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showError = value
            )
        }
    }

    fun updateIsUpdateSuccessful(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isUpdateSuccessful = value
            )
        }
    }

    fun validateFields(): Boolean {
        val state = _uiState.value

        if (state.name.isBlank()) {
            updateShowError("Name cannot be empty")
            return false
        }

        if (state.administratorFirstName.isBlank()) {
            updateShowError("Administrator's first name cannot be empty")
            return false
        }

        if (state.administratorLastName.isBlank()) {
            updateShowError("Administrator's last name cannot be empty")
            return false
        }

        if (state.country.isBlank()) {
            updateShowError("Country cannot be empty")
            return false
        }

        if (state.identificationNumber.isBlank()) {
            updateShowError("Identification number cannot be empty")
            return false
        }

        // Example of additional format check (optional)
        if (!state.identificationNumber.all { it.isDigit() }) {
            updateShowError("Identification number must be numeric")
            return false
        }

        // No errors
        _uiState.update { it.copy(showError = false, errorMessage = "") }
        return true
    }

    fun resetFields() {
        _uiState.update {
            OrganizerUpdateDetailsUiState(
                name = "",
                administratorFirstName = "",
                administratorLastName = "",
                country = "",
                identificationNumber = "",
                showError = false,
                errorMessage = "",
                isUpdateSuccessful = false
            )
        }
    }


    fun onDoneClick(
        organizerUuid: String
    ) {
        updateError(false)

        // Update Database, on succes go to confirmation screen
        if(isFormValid.value) {
            if(validateFields()) {
                viewModelScope.launch {

                    val organizerInfo = appRepository.updateOrganizerDetails(
                        organizerUuid = organizerUuid,
                        name = _uiState.value.name,
                        administratorLastName = _uiState.value.administratorLastName,
                        identificationNumber = _uiState.value.identificationNumber,
                        administratorFirstName = _uiState.value.administratorFirstName,
                        country = _uiState.value.country
                    )

                    if(organizerInfo.organizerUuid != "-1") {
                        Log.d("Org Update Details", "Trying to update user pref and isSuccessful")
                        userPreferencesRepository.saveSupabaseOrganizerInfo(organizerInfo)
                        updateIsUpdateSuccessful(true)
                    }
                    else {
                     updateShowError("Something went wrong...")
                        updateIsUpdateSuccessful(false)
                    }
                }
            }
        }

    }



    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                OrganizerUpdateDetailsScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class OrganizerUpdateDetailsUiState(
    val name: String,
    val administratorFirstName: String,
    val administratorLastName: String,
    val country: String,
    val identificationNumber: String,
    val showError: Boolean,
    val errorMessage: String,
    val isUpdateSuccessful: Boolean = false
)