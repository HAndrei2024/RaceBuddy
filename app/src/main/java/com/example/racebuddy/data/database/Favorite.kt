package com.example.racebuddy.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(
            entity = Athlete::class,
            parentColumns = ["id"],
            childColumns = ["athlete_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["athlete_id", "event_id"]
)
data class Favorite(
    @ColumnInfo(name = "athlete_id")
    val athleteId: Int,
    @ColumnInfo(name = "event_id")
    val eventId: Int
)

