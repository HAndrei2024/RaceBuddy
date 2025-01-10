package com.example.racebuddy.models

data class Result(
    val id: Int,
    val race: String, // Name or identifier of the race
    val position: Int, // Athlete's position in the race
    val time: String, // Time taken for the race (could be in HH:MM:SS format)
    val stravaInfo: StravaInfo
)
