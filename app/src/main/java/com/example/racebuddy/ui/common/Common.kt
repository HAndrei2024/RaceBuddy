package com.example.racebuddy.ui.common

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopAppBar(
    title: String = "RaceBuddy"
) {
    CenterAlignedTopAppBar(
        title = { Text(title) }
    )
}