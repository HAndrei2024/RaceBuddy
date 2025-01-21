package com.example.racebuddy.data.database

import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val athleteDao: AthleteDao,
    private val eventDao: EventDao,
    private val resultDao: ResultDao
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

    fun checkFavoriteEvent(userId: Int, eventId: Int): Flow<Int> {
        return eventDao.checkFavoriteEvent(userId, eventId)
    }

    fun getListOfFavoriteEvents(athleteId: Int): Flow<List<Event>> {
        return eventDao.getFavoriteListOfEvents(athleteId)
    }

    suspend fun addFavoriteEvent(athleteId: Int, eventId: Int) {
        eventDao.insertFavoriteEvent(athleteId, eventId)
    }

    suspend fun removeFavoriteEvent(athleteId: Int, eventId: Int) {
        eventDao.deleteFavoriteEvent(athleteId, eventId)
    }

    fun getAthlete(id: Int): Flow<Athlete> {
        return athleteDao.getAthlete(id)
    }

    fun getEvent(id: Int): Flow<Event> {
        return eventDao.getEvent(id)
    }

    suspend fun insertResult(result: Result) {
        resultDao.insertResult(result)
    }

    fun getAllEventResults(eventId: Int): Flow<List<Result>> {
        return resultDao.getAllEventResults(eventId)
    }

    fun getAllAthleteResults(athleteId: Int): Flow<List<Result>> {
        return resultDao.getAllAthleteResults(athleteId)
    }

    suspend fun getAllEventResultsNotFlow(eventId: Int): List<Result> {
        return resultDao.getAllEventResultsNotFlow(eventId)
    }
}