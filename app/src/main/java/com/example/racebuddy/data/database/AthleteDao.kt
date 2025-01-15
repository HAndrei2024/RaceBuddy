package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query("SELECT id FROM athlete WHERE username = :username AND password = :password")
    suspend fun verifyLogin(username: String, password: String): Int

    @Query("SELECT username FROM athlete WHERE id = :id")
    suspend fun getUsernameById(id: Int): String

    @Query("SELECT * FROM athlete WHERE id = :id")
    fun getAthlete(id: Int): Flow<Athlete> //Flow or no flow?

    @Insert
    suspend fun createNewAccount(athlete: Athlete)
}