package com.example.racebuddy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE title LIKE '%' || :title || '%'")
    fun getListOfEvents(title: String): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id IN (" +
            "SELECT event_id FROM favorite WHERE athlete_id = :id)")
    fun getFavoriteListOfEvents(id: Int): Flow<List<Event>>

    @Query("SELECT COUNT(*) FROM favorite WHERE athlete_id = :userId AND event_id = :eventId")
    fun checkFavoriteEvent(userId: Int, eventId: Int): Flow<Int>

    @Query("INSERT INTO favorite VALUES (:userId, :eventId)")
    suspend fun insertFavoriteEvent(userId: Int, eventId: Int)


    @Query("DELETE FROM favorite WHERE athlete_id = :athleteId AND event_id = :eventId")
    suspend fun deleteFavoriteEvent(athleteId: Int, eventId: Int)

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEvent(eventId: Int): Flow<Event>

    @Insert
    suspend fun insertEvent(event: Event)
}