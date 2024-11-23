package com.example.racebuddy.data

import androidx.annotation.DrawableRes
import java.sql.Date

data class Event(
    @DrawableRes val picture: Int?, // Drawable resource ID for event image
    val title: String,
    val location: Location,
    val date: Date,
    val organizer: Organizer,
    val athletes: List<Athlete>?,
    val results: List<Result>?,
    val details: String?,
    val track: Track?,
    val posts: List<Post>? // List of posts related to the event (could be URLs or other identifiers)
)