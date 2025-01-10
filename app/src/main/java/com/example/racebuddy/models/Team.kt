package com.example.racebuddy.models

import androidx.annotation.DrawableRes
import com.example.racebuddy.R

data class Team(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val reigstrationNumber: Int,
    @DrawableRes val profileImage: Int = R.drawable.default_profile,
    val country: String, //IsoCountryCode API
    val sponsors: List<String> = emptyList(),
    val athletes: List<Athlete> = emptyList(),
    val results: List<Result> = emptyList()
)
