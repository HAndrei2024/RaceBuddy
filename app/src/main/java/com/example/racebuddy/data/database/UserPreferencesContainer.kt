package com.example.racebuddy.data.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val USER_PREFERENCE_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

class UserPreferencesContainer(private val context: Context) {

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(
            dataStore = context.dataStore
        )
    }
}