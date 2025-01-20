package com.example.racebuddy.data.database

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.racebuddy.R

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @DrawableRes val picture: Int = R.drawable.default_background, // Drawable resource ID for event image
    val title: String,
    @ColumnInfo(name = "start_date_string")
    val startDateString: String,
    @ColumnInfo(name = "end_date_string")
    val endDateString: String,
    @ColumnInfo(name = "county")
    val county: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "location_name")
    val locationName: String,
    @ColumnInfo(name = "organizer_name")
    val organizerName: String,
    @ColumnInfo(name = "description")
    val description: String
)
