package com.example.racebuddy.models

data class StravaInfo(
    val averageSpeed: Float, // Average speed in km/h or mph
    val maxSpeed: Float, // Max speed in km/h or mph
    val averageHeartRate: Int, // Average heart rate in bpm
    val maxHeartRate: Int // Max heart rate in bpm
)
