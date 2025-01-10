package com.example.racebuddy.models

import androidx.annotation.DrawableRes
import com.example.racebuddy.R

data class Organizer(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val reigstrationNumber: Int,
    @DrawableRes val profileImage: Int = R.drawable.default_profile,
    val events: List<Competition> = emptyList()
    )
