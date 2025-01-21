package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Insert
    suspend fun insertResult(result: Result)

    @Query("SELECT * FROM result WHERE event_id = :eventId")
    fun getAllEventResults(eventId: Int): Flow<List<Result>>

    @Query("SELECT * FROM result WHERE athlete_id = :athleteId")
    fun getAllAthleteResults(athleteId: Int): Flow<List<Result>>

    @Query("SELECT * FROM result WHERE event_id = :eventId")
    suspend fun getAllEventResultsNotFlow(eventId: Int): List<Result>
}