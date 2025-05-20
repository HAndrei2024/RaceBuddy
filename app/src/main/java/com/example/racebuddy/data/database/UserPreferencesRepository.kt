package com.example.racebuddy.data.database

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ATHLETE_LOGIN_ID = intPreferencesKey("ATHLETE_LOGIN_ID")
        val SUPABASE_ATHLETE_ID = stringPreferencesKey("SUPABASE_ATHLETE_ID")
    }

    val athleteLoginId: Flow<Int> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("UserPreferencesRepository", "IOException", it)
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { preferences ->
            preferences[ATHLETE_LOGIN_ID] ?: -1
        }

    val supabaseAthleteId: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("UserPreferencesRepository", "IOException", it)
                emit(emptyPreferences())
            }
            else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SUPABASE_ATHLETE_ID] ?: ""
        }

    suspend fun saveAthleteLoginId(loginId: Int) {
        dataStore.edit { preferences ->
            preferences[ATHLETE_LOGIN_ID] = loginId
        }
    }

    suspend fun saveSupabaseAthleteId(athleteId: String) {
        dataStore.edit { preferences ->
            preferences[SUPABASE_ATHLETE_ID] = athleteId
        }
    }

}