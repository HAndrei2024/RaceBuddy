package com.example.racebuddy.ui.v2.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("This is Main Screen")
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}