package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE title LIKE '%' || :title || '%'")
    fun getListOfEvents(title: String): Flow<List<Event>>

    @Insert
    suspend fun insertEvent(event: Event)
}