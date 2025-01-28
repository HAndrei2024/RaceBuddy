package com.example.racebuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.racebuddy.data.database.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Athlete::class, Event::class, Favorite::class, Result::class], version = 12)
abstract class AppDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
    abstract fun eventDao() : EventDao
    abstract fun resultDao() : ResultDao

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
            val resultDao = getDatabase(context).resultDao()
            val events = listOf(
                Event(
                    title = "Music Festival 2025",
                    startDateString = "2025-05-15T10:00:00",
                    endDateString = "2025-05-15T18:00:00",
                    county = "Orange County",
                    country = "USA",
                    city = "Los Angeles",
                    locationName = "Hollywood Park",
                    organizerName = "Some firm",
                    description = "A day-long music festival featuring top artists from around the world."
                ),
                Event(
                    title = "Tech Conference 2025",
                    startDateString = "2025-06-20T09:00:00",
                    endDateString = "2025-06-22T18:00:00",
                    county = "Santa Clara County",
                    country = "USA",
                    city = "San Francisco",
                    locationName = "Moscone Center",
                    organizerName = "Another firm",
                    description = "The largest tech conference in the world, showcasing the latest in technology."
                ),
                Event(
                    title = "Art Exhibition 2025",
                    startDateString = "2025-07-01T11:00:00",
                    endDateString = "2025-07-31T19:00:00",
                    county = "Cook County",
                    country = "USA",
                    city = "Chicago",
                    locationName = "The Art Institute of Chicago",
                    organizerName = "Yet another firm",
                    description = "A month-long exhibition of modern art from renowned artists."
                )
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
            val results = listOf(
                Result(
                    time = "1:00:00",
                    athleteId = 1,
                    eventId = 1
                ),
                Result(
                    time = "2:00:00",
                    athleteId = 1,
                    eventId = 2
                ),
                Result(
                    time = "3:00:00",
                    athleteId = 1,
                    eventId = 3
                )
            )
            CoroutineScope(Dispatchers.IO).launch {
                events.forEach { event ->
                    eventDao.insertEvent(event)
                }
                athletes.forEach { athlete ->
                    athleteDao.createNewAccount(athlete)
                }
                results.forEach { result ->
                    resultDao.insertResult(result)
                }
            }
        }
    }
}