package com.example.racebuddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.racebuddy.app.v1.App
import com.example.racebuddy.app.v2.Appv2
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.LocalDataSource
import com.example.racebuddy.data.database.RemoteDataSource
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.data.network.StravaApi
import com.example.racebuddy.data.network.StravaApiService
import com.example.racebuddy.data.network.StravaScreenIntent
import com.example.racebuddy.ui.v1.event.EventScreen
import com.example.racebuddy.ui.theme.RaceBuddyTheme
import com.example.racebuddy.ui.v2.event.EventScreen2
import com.example.racebuddy.ui.v2.event.EventScreenCollapsing
import com.example.racebuddy.ui.v2.login.LoginScreen
import com.example.racebuddy.ui.v2.signup.BirthdateInputFields
import com.example.racebuddy.ui.v2.signup.BirthdateTextField
import com.example.racebuddy.ui.v2.signup.SignupScreensViewModel
import com.example.racebuddy.ui.v2.signup.SignupSecondScreen
import com.example.racebuddy.ui.v2.signup.TestDate
import com.example.racebuddy.ui.v2.signup.TestDateScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = intent
        setContent {
            RaceBuddyTheme {
                //App()
                Appv2()
            }
        }
    }
}