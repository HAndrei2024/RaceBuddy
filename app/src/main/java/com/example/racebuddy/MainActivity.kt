package com.example.racebuddy

import BasicMapScreen
import MapScreen
import OSMMapScreen
import UpdatedOSMMapScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.LocalDataSource
import com.example.racebuddy.ui.event.EventScreen
import com.example.racebuddy.ui.theme.RaceBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaceBuddyTheme {
            UpdatedOSMMapScreen(
                context = applicationContext,
                address = "Bucharest"
            )
            //OSMMapScreen(context = applicationContext)
            //App()
//                EventScreen(
//                    modifier = Modifier
//                )
            }
        }
    }
}

