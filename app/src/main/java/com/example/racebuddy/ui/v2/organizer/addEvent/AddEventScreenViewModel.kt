package com.example.racebuddy.ui.v2.organizer.addEvent

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.racebuddy.Application
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.AthleteCategory
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
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import network.chaintech.kmp_date_time_picker.utils.now

class AddEventScreenViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddEventScreenUiState(
            title = "",
            eventCategory = "",
            country = "",
            city = "",
            county = "",
            startDay = "",
            startMonth = "",
            startYear = LocalDate.now().year.toString(),
            endDay = "",
            endMonth = "",
            endYear = LocalDate.now().year.toString(),
            details = "",
            athleteCategories = listOf(List(3) { "" }),
            isAddSuccessful = false,
            showError = false
        )
    )
    val uiState = _uiState.asStateFlow()

//    val isFormValid: State<Boolean> = derivedStateOf {
//        listOf(
//            _uiState.value.title,
//            _uiState.value.eventCategory,
//            _uiState.value.country,
//            _uiState.value.city,
//            _uiState.value.county,
//            _uiState.value.startDay,
//            _uiState.value.endDay,
//            _uiState.value.startMonth,
//            _uiState.value.endMonth,
//            _uiState.value.details
//        ).all { it.isNotBlank() }
//    }

    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        listOf(
            state.title,
            state.eventCategory,
            state.country,
            state.city,
            state.county,
            state.startDay,
            state.endDay,
            state.startMonth,
            state.endMonth,
            state.details
        ).all { it.isNotBlank() }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun onTitleChange(value: String) {
       _uiState.update { currentState ->
           currentState.copy(
               title = value
           )
       }
    }

    fun onEventCategoryFilterClick(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                eventCategory = category
            )
        }
    }

    fun onCountryChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                country = value
            )
        }
    }

    fun onCityChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                city = value
            )
        }
    }

    fun onCountyChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                county = value
            )
        }
    }

    fun onStartDayChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                startDay = value
            )
        }
    }

    fun onEndDayChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
               endDay = value
            )
        }
    }

    fun onStartMonthChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
               startMonth = value
            )
        }
    }

    fun onEndMonthChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
               endMonth = value
            )
        }
    }

    fun onDetailsChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                details = value
            )
        }
    }

    fun addAthleteCategoryRow() {
        _uiState.update { currentState ->
            currentState.copy(
                athleteCategories = _uiState.value.athleteCategories + listOf(List(3) { "" })
            )
        }
    }

    fun removeAthleteCategoryRow(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                athleteCategories = _uiState.value.athleteCategories.toMutableList().apply { removeAt(index) }
            )
        }
    }

    fun updateCategoryRowCell(rowIndex: Int, columnIndex: Int, value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                athleteCategories = _uiState.value.athleteCategories.mapIndexed { rwIndex, row ->
                    if (rwIndex == rowIndex) {
                        row.mapIndexed { cIdx, cell ->
                            if (cIdx == columnIndex) value else cell
                        }
                    } else row
                }
            )
        }
    }

    fun onAddEvent(
        organizerUuid: String
    ) {
        if(isFormValid.value ) {
            if(validateFields(uiState.value)) {

                updateShowError(false)
                Log.d("AddEventVM", "Add Event Button pressed: data = ${_uiState.value.startDay}")

                val categories = convertToCategoryList(_uiState.value.athleteCategories)

                val jsonCategories: JsonElement = JsonArray(categories.map {
                    Json.encodeToJsonElement(it)
                })


                viewModelScope.launch {
                    val eventUuid = appRepository.addSupabaseEvent(
                        title = _uiState.value.title,
                        startDate = formatDate(
                            _uiState.value.startDay,
                            _uiState.value.startMonth,
                            _uiState.value.startYear
                        ),
                        endDate = formatDate(
                            _uiState.value.endDay,
                            _uiState.value.endMonth,
                            _uiState.value.endYear
                        ),
                        country = _uiState.value.country,
                        city = _uiState.value.city,
                        county = _uiState.value.county,
                        details = _uiState.value.details,
                        eventCategory = _uiState.value.eventCategory,
                        athleteCategories = jsonCategories,
                        organizerUuid = organizerUuid
                    )

                    Log.d("AddEventViewModel", "Event added: $eventUuid")

                    if (eventUuid != "-1") {
                        Log.d("AddEventVM", "event Uuiid != -1, going to update isSuccessful...")
                        updateIsAddSuccessful(true)
                        updateShowError(false)
                    } else {
                        updateIsAddSuccessful(false)
                        updateShowError(true)
                    }
                }
            }
            else {
                updateShowError(true)
            }
        }
    }

    fun resetFields() {
        val defaultState = AddEventScreenUiState(
            title = "",
            eventCategory = "",
            country = "",
            city = "",
            county = "",
            startDay = "",
            startMonth = "",
            startYear = LocalDate.now().year.toString(),
            endDay = "",
            endMonth = "",
            endYear = LocalDate.now().year.toString(),
            details = "",
            athleteCategories = listOf(List(3) { "" }),
            isAddSuccessful = false,
            showError = false
        )

        _uiState.value = defaultState
    }


    fun updateIsAddSuccessful(value: Boolean) {
        Log.d("AddEventVM", "update isSuccessful... $value")
        _uiState.update { currentValue ->
            currentValue.copy(
                isAddSuccessful = value
            )
        }
    }

    fun updateShowError(value: Boolean) {
        _uiState.update { currentValue ->
            currentValue.copy(
                showError = value
            )
        }
    }

    fun validateFields(state: AddEventScreenUiState): Boolean {
        with(state) {
            if (title.length < 5) {
                updateErrorMessage("Title must be at least 5 characters long.")
                return false
            }
            if (country.isBlank() || county.isBlank() || city.isBlank()) {
                updateErrorMessage("Country, county, and city cannot be empty.")
                return false
            }
            if (details.isBlank()) {
                updateErrorMessage("Details cannot be empty.")
                return false
            }
            if (athleteCategories.isEmpty()) {
                updateErrorMessage("At least one athlete category must be specified.")
                return false
            }

            // Try parsing and comparing dates
            val startDate = try {
                "${startYear.padStart(4, '0')}-${startMonth.padStart(2, '0')}-${startDay.padStart(2, '0')}"
                    .let { LocalDate.parse(it) }
            } catch (e: Exception) {
                updateErrorMessage("Invalid start date.")
                return false
            }

            val endDate = try {
                "${endYear.padStart(4, '0')}-${endMonth.padStart(2, '0')}-${endDay.padStart(2, '0')}"
                    .let { LocalDate.parse(it) }
            } catch (e: Exception) {
                updateErrorMessage("Invalid end date.")
                return false
            }

            if (endDate < startDate) {
                updateErrorMessage("End date must be after start date.")
                return false
            }

            if(startDate < LocalDate.now()) {
                updateErrorMessage("Start date should be in the future.")
                return false
            }

            if(endDate < LocalDate.now()) {
                updateErrorMessage("End date should be in the future.")
                return false
            }

            return true
        }
    }


    fun updateErrorMessage(message: String) {
        _uiState.update { currentValue ->
            currentValue.copy(
                errorMessage = message
            )
        }
    }

    fun convertToCategoryList(data: List<List<String>>): List<AthleteCategory> {
        Log.d("AddEventVM", "Trying to convert categories to AthleteCategory... $data")
        return data.map { item ->
            AthleteCategory(
                maxAge = item[2].toInt(),
                minAge = item[1].toInt(),
                category = item[0]
            )
        }
    }

    private fun formatDate(day: String, month: String, year: String): LocalDate {

        val formattedDate = LocalDate(year.toInt(), month.toInt(), day.toInt())

        return formattedDate
    }



    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                AddEventScreenViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesContainer.userPreferencesRepository
                )
            }
        }
    }
}

data class AddEventScreenUiState(
    val title: String,
    val eventCategory: String,
    val country: String,
    val city: String,
    val county: String,
    val startDay: String,
    val startMonth: String,
    val startYear: String,
    val endDay: String,
    val endMonth: String,
    val endYear: String,
    val details: String,
    val athleteCategories: List<List<String>>,
    val isAddSuccessful: Boolean,
    val showError: Boolean,
    val errorMessage: String = ""
)
