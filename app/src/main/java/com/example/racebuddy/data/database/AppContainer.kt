package com.example.racebuddy.data.database

import android.content.Context

interface AppContainer {
    val appRepository: AppRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val appRepository: AppRepository by lazy {
        AppRepository(
            localDataSource = LocalDataSource(AppDatabase.getDatabase(context).athleteDao()),
            remoteDataSource = RemoteDataSource()
        )
    }
}