package com.example.racebuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Athlete::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao

    companion object {
        @Volatile // changes made to one thread are visible to all
        private var Instance: AppDatabase? = null
        // keeps a reference to the db, so only 1 is created

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}