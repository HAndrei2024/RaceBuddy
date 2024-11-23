package com.example.racebuddy.data

import androidx.annotation.DrawableRes
import java.sql.Time

data class Post(
    val organizer: Organizer,
    val title: String?,
    val description: String,
    @DrawableRes val image: Int?,
    val time: Time
)
