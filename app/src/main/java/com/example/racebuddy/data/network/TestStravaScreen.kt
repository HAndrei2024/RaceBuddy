package com.example.racebuddy.data.network

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.racebuddy.BuildConfig
import com.example.racebuddy.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun StravaScreenIntent(
    onAuthorizeClick: () -> Unit,
    onGetDataClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val intent = activity.intent
    val response = intent.data?.getQueryParameter("code").toString()
    val stravaAthlete = StravaAthlete(0, "", 0, "", "", "")
    var stravaAuthResult by remember { mutableStateOf(StravaAuthResponse("", 0, "", "", stravaAthlete)) }
    var jsonString by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onAuthorizeClick
        ) {
            Text(
                text = "Authorization screen"
            )
        }
        Text(
            text = response
        )
        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    stravaAuthResult = StravaApi.retrofitService.getAuthDetails(
                        clientId = BuildConfig.CLIENT_ID,
                        clientSecret = BuildConfig.CLIENT_SECRET,
                        authorizationCode = response
                    )
                }
            }
        ) {
            Text(
                text = "Get Athlete data"
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(stravaAuthResult.athlete.profilePictureUrl)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_launcher_background),
            placeholder = painterResource(R.drawable.default_profile),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop
        )
    }
}
