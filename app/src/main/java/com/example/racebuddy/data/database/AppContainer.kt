package com.example.racebuddy.data.database

import android.content.Context

interface AppContainer {
    val appRepository: AppRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val appRepository: AppRepository by lazy {
        AppRepository(
            localDataSource = LocalDataSource(
                athleteDao = AppDatabase.getDatabase(context).athleteDao(),
                eventDao = AppDatabase.getDatabase(context).eventDao(),
                resultDao = AppDatabase.getDatabase(context).resultDao()
            ),
            remoteDataSource = RemoteDataSource()
        )
    }

    init {
        //AppDatabase.populateDatabase(context)
    }
}