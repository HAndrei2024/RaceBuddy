package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AthleteDao {
    @Query("SELECT COUNT(*) FROM athlete WHERE username = :username AND password = :password")
    suspend fun verifyLogin(username: String, password: String): Int

    @Insert
    suspend fun createNewAccount(athlete: Athlete)
}