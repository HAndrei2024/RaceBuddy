package com.example.racebuddy

import android.app.Application
import com.example.racebuddy.data.database.AppContainer
import com.example.racebuddy.data.database.AppDataContainer

class Application : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}