package com.example.racebuddy.data.database

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.racebuddy.models.Result
import com.example.racebuddy.models.Team
import java.sql.Date

@Entity(tableName = "athlete")
data class Athlete(
    @PrimaryKey
    val id: Int,
    val name: String,
    val surname: String,
    val username: String,
    val password: String
)
