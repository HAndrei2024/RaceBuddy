package com.example.racebuddy.data.database

class LocalDataSource(val athleteDao: AthleteDao) {
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
}