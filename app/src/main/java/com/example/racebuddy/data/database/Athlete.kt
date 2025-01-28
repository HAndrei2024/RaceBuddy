package com.example.racebuddy.data.database

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.racebuddy.models.Result
import com.example.racebuddy.models.Team
import java.sql.Date

@Entity(
    tableName = "athlete",
    indices = [Index(value = ["username"], unique = true)]
)
data class Athlete(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val username: String,
    val password: String,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String = ""
)
