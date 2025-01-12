package com.example.racebuddy.data.database

class LocalDataSource(private val athleteDao: AthleteDao) {
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

}