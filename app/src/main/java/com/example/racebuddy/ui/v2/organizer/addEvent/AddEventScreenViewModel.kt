package com.example.racebuddy.ui.v2.organizer.addEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
            startYear = "",
            endDay = "",
            endMonth = "",
            endYear = "",
            details = "",
            athleteCategories = listOf(List(3) { "" }),
        )
    )
    val uiState = _uiState.asStateFlow()

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
    val athleteCategories: List<List<String>>
)
