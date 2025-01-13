package com.example.racebuddy.data.database

import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val athleteDao: AthleteDao,
    private val eventDao: EventDao
) {
    fun getData() {

    }

    suspend fun verifyLogin(
        username: String,
        password: String
    ) : Int {
        return athleteDao.verifyLogin(username, password)
    }

    suspend fun createNewAccount(athlete: Athlete) {
        athleteDao.createNewAccount(athlete)
    }

    suspend fun getUsernameById(id: Int): String {
        return athleteDao.getUsernameById(id)
    }

    fun getListOfEvents(title: String): Flow<List<Event>> {
        return eventDao.getListOfEvents(title)
    }

}