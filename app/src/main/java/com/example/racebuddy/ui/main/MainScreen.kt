package com.example.racebuddy.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racebuddy.ui.common.LoginTopAppBar

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.factory
    )
) {
    val mainScreenUiState = mainScreenViewModel.uiState.collectAsState()
    Scaffold(
        topBar = { LoginTopAppBar("RaceBuddy") }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "Main Screen! Hello ${mainScreenUiState.value.athleteUsername}")
        }
    }
}