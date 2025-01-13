package com.example.racebuddy.data.database

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.racebuddy.R

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @DrawableRes val picture: Int = R.drawable.default_background, // Drawable resource ID for event image
    val title: String
)
