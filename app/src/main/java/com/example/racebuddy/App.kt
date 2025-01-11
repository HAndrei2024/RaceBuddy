package com.example.racebuddy

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.racebuddy.ui.login.LoginScreen

@Composable
fun App() {
    LoginScreen(
        title = "RaceBuddy"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopAppBar(
    title: String
) {
    CenterAlignedTopAppBar(
        title = { Text(title) }
    )
}