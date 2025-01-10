package com.example.racebuddy.models

import androidx.annotation.DrawableRes
import java.sql.Date

data class Athlete(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val birthDate: Date,
    val licensed: Boolean = false,
    val reigstrationNumber: Int = 0,
    val team: Team?,
    @DrawableRes val profileImage: Int?,
    val country: String, //IsoCountryCode API
    val sponsors: List<String> = emptyList(),
    val results: List<Result> = emptyList()
)



