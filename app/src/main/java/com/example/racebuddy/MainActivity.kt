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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.LocalDataSource
import com.example.racebuddy.data.network.StravaApi
import com.example.racebuddy.data.network.StravaApiService
import com.example.racebuddy.data.network.StravaScreenIntent
import com.example.racebuddy.ui.event.EventScreen
import com.example.racebuddy.ui.theme.RaceBuddyTheme
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
                StravaScreenIntent(
                    onAuthorizeClick = {
                        val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                            .buildUpon()
                            .appendQueryParameter("client_id", "146392")
                            .appendQueryParameter("redirect_uri", "myapp://localhost")
                            .appendQueryParameter("response_type", "code")
                            .appendQueryParameter("approval_prompt", "auto")
                            .appendQueryParameter("scope", "activity:write,read")
                            .build()

                        // Create an Intent to open the OAuth URL in a browser
                        val intentFromButton = Intent(Intent.ACTION_VIEW, intentUri)

                        // Start the activity (launch the browser with the OAuth URL)
                        startActivity(intentFromButton)
                    },
                    onGetDataClick = {
                    }
                )
            }
        }
    }
}


@Composable
fun Strava() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                    .buildUpon()
                    .appendQueryParameter("client_id", "146392")
                    .appendQueryParameter("redirect_uri", "myapp://localhost")
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("approval_prompt", "auto")
                    .appendQueryParameter("scope", "activity:write,read")
                    .build()

                // Create an Intent to open the OAuth URL in a browser
                val intentFromButton = Intent(Intent.ACTION_VIEW, intentUri)

                // Start the activity (launch the browser with the OAuth URL)
                //startActivity(intentFromButton)

            }
        ) {
            Text(
                text = "Authorization screen?"
            )
        }
//        Text(
//            text = intent.data?.getQueryParameter("code").toString()
//        )
    }
}

