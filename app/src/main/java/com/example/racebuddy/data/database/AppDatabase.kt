package com.example.racebuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Athlete::class, Event::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
    abstract fun eventDao() : EventDao

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

        fun populateDatabase(context: Context) {
            // Insert initial data (for example, inserting items)
            val eventDao = getDatabase(context).eventDao()
            val athleteDao = getDatabase(context).athleteDao()
            val events = listOf(
                Event(title = "Event 1"),
                Event(title = "Event 2"),
                Event(title = "Event 3")
            )
            val athletes = listOf(
                Athlete(
                    name = "t1",
                    surname = "t1",
                    username = "t1",
                    password = "t1"
                ),
                Athlete(
                    name = "t2",
                    surname = "t2",
                    username = "t2",
                    password = "t2"
                )
            )
            CoroutineScope(Dispatchers.IO).launch {
                events.forEach { event ->
                    eventDao.insertEvent(event)
                }
                athletes.forEach { athlete ->
                    athleteDao.createNewAccount(athlete)
                }
            }
        }
    }
}