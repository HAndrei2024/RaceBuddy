package com.example.racebuddy.data

import androidx.annotation.DrawableRes
import java.sql.Date

data class Team(
    val name: String,
    val email: String,
    val password: String,
    val reigstrationNumber: Int? = null,
    @DrawableRes val profileImage: Int?,
    val country: String, //IsoCountryCode API
    val sponsors: List<String>?,
    val athletes: List<Athlete>?,
    val results: List<Result>?
)
