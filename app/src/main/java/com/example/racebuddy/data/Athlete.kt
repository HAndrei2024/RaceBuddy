package com.example.racebuddy.data

import androidx.annotation.DrawableRes
import java.sql.Date
import java.util.Locale.IsoCountryCode

data class Athlete(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val birthDate: Date,
    val licensed: Boolean = false,
    val reigstrationNumber: Int? = null,
    val team: Team?,
    @DrawableRes val profileImage: Int?,
    val country: String, //IsoCountryCode API
    val sponsors: List<String>?,
    val results: List<Result>?
)
