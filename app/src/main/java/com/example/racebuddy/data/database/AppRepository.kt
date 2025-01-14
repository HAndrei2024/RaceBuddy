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
}
