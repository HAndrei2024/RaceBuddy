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
    suspend fun getAllAthleteResults(athleteId: Int): List<Result>

    @Query("SELECT * FROM result WHERE event_id = :eventId")
    suspend fun getAllEventResultsNotFlow(eventId: Int): List<Result>

    @Query("SELECT COUNT(*) FROM result WHERE athlete_id = :athleteId AND event_id = :eventId")
    fun checkIfAthleteRegistered(athleteId: Int, eventId: Int): Flow<Int>
}