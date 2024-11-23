package com.example.racebuddy.data

import androidx.annotation.DrawableRes

data class Organizer(
    val name: String,
    val email: String,
    val password: String,
    val reigstrationNumber: Int? = null,
    @DrawableRes val profileImage: Int?,
    val events: List<Event>?
    )
