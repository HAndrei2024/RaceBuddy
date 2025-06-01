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
import kotlinx.serialization.json.Json
import java.io.IOException


class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ATHLETE_LOGIN_ID = intPreferencesKey("ATHLETE_LOGIN_ID")
        val SUPABASE_ATHLETE_INFO = stringPreferencesKey("SUPABASE_ATHLETE_INFO")
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

    val supabaseAthleteInfo: Flow<AthleteInfo> = dataStore.data
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
            preferences[SUPABASE_ATHLETE_INFO] ?: Json.encodeToString(testAthlete)
        }.map { jsonString ->
            try {
                Json.decodeFromString<AthleteInfo>(jsonString)
            } catch (e: Exception) {
                Log.e("UserParseError", "Invalid JSON: $jsonString", e)
                null
            }!!
        }

    suspend fun saveAthleteLoginId(loginId: Int) {
        dataStore.edit { preferences ->
            preferences[ATHLETE_LOGIN_ID] = loginId
        }
    }

    suspend fun saveSupabaseAthleteInfo(athleteInfo: AthleteInfo) {
        dataStore.edit { preferences ->
            preferences[SUPABASE_ATHLETE_INFO] = Json.encodeToString(athleteInfo)
        }
    }

    suspend fun logoutSupabaseAthleteInfo() {
        dataStore.edit { prefereces ->
            prefereces[SUPABASE_ATHLETE_INFO] = Json.encodeToString(testAthlete)
        }
    }

}