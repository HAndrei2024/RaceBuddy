package com.example.racebuddy.data.database

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    fun getData() {
        localDataSource.getData()
    }

    suspend fun verifyLogin(
        username: String,
        password: String
    ) : Int {
        return localDataSource.verifyLogin(username, password)
    }

    suspend fun createNewAccount(athlete: Athlete) {
        localDataSource.createNewAccount(athlete)
    }

    suspend fun getUsernameById(id: Int): String {
        return localDataSource.getUsernameById(id)
    }

    fun getListOfEvents(title: String): Flow<List<Event>> {
        return localDataSource.getListOfEvents(title)
    }

    fun checkFavoriteEvent(userId: Int, eventId: Int): Flow<Int> {
        return localDataSource.checkFavoriteEvent(userId, eventId)
    }

    fun getListOfFavoriteEvents(athleteId: Int): Flow<List<Event>> {
        return localDataSource.getListOfFavoriteEvents(athleteId)
    }

    suspend fun addFavoriteEvent(athleteId: Int, eventId: Int) {
        localDataSource.addFavoriteEvent(athleteId, eventId)
    }

    suspend fun removeFavoriteEvent(athleteId: Int, eventId: Int) {
        localDataSource.removeFavoriteEvent(athleteId, eventId)
    }

    fun getAthlete(id: Int): Flow<Athlete> {
        return localDataSource.getAthlete(id)
    }

    fun getEvent(id: Int): Flow<Event> {
        return localDataSource.getEvent(id)
    }

    suspend fun insertResult(result: Result) {
        localDataSource.insertResult(result)
    }

    fun getAllEventResults(eventId: Int): Flow<List<Result>> {
        return localDataSource.getAllEventResults(eventId)
    }

    suspend fun getAllAthleteResults(athleteId: Int): List<Result> {
        return localDataSource.getAllAthleteResults(athleteId)
    }

    suspend fun getAllEventResultsNotFlow(eventId: Int): List<Result> {
        return localDataSource.getAllEventResultsNotFlow(eventId)
    }

    fun checkIfAthleteRegistered(athleteId: Int, eventId: Int): Flow<Int> {
        return localDataSource.checkIfAthleteRegistered(athleteId, eventId)
    }
    suspend fun updateAthleteProfilePicture(profilePictureUrl: String, id: Int){
        localDataSource.updateAthleteProfilePicture(
            profilePictureUrl = profilePictureUrl,
            id = id
        )
    }
}
