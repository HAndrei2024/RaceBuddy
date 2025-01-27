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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racebuddy.BuildConfig
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
    var result by remember { mutableStateOf("") }
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
                    result = StravaApi.retrofitService.getAccessToken(
                        clientId = BuildConfig.CLIENT_ID,
                        clientSecret = BuildConfig.CLIENT_SECRET,
                        authorizationCode = response
                    ).string()
                }
            }
        ) {
            Text(
                text = "Get Athlete data"
            )
        }
        Text(
            text = result
        )
    }
}
