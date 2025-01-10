package com.example.racebuddy.models

import androidx.annotation.DrawableRes
import java.sql.Time

data class Post(
    val id: Int,
    val organizer: Organizer,
    val title: String?,
    val description: String,
    @DrawableRes val image: Int?,
    val time: Time
)
