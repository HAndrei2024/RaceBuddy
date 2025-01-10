package com.example.racebuddy.models

import androidx.annotation.DrawableRes
import com.example.racebuddy.R
import java.sql.Date

data class Competition(
    val id: Int,
    @DrawableRes val picture: Int = R.drawable.default_background, // Drawable resource ID for event image
    val title: String,
    val location: Location,
    val date: Date,
    val organizer: Organizer,
    val athletes: List<Athlete> = emptyList(),
    val results: List<Result> = emptyList(),
    val details: String = "",
    val track: Track,
    val posts: List<Post> = emptyList() // List of posts related to the event (could be URLs or other identifiers)
)