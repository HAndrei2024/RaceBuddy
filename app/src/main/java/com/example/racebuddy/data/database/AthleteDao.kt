package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AthleteDao {
    @Query("SELECT id FROM athlete WHERE username = :username AND password = :password")
    suspend fun verifyLogin(username: String, password: String): Int

    @Query("SELECT username FROM athlete WHERE id = :id")
    suspend fun getUsernameById(id: Int): String

    @Insert
    suspend fun createNewAccount(athlete: Athlete)
}