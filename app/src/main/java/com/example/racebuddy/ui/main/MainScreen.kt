package com.example.racebuddy.ui.main

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.ui.common.BottomAppBar
import com.example.racebuddy.ui.common.EventCard
import com.example.racebuddy.ui.common.LoginTopAppBar
import com.example.racebuddy.ui.common.MainScreenTopAppBar

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.factory
    ),
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    isHomeSelected: Boolean = true,
    isFavoriteSelected: Boolean = false,
    isProfileSelected: Boolean = false,
    onEventClick: (Event) -> Unit = { }
) {
    val mainScreenUiState = mainScreenViewModel.uiState.collectAsState()
    val eventList = mainScreenUiState.value.eventList.collectAsState(emptyList())
    val favoriteEvents = mainScreenUiState.value.favoriteEvents

    Scaffold(
        topBar = { LoginTopAppBar("RaceBuddy") },
        bottomBar = { BottomAppBar(
            onHomeClick = onHomeClick,
            onFavoriteClick = onFavoriteClick,
            onProfileClick = onProfileClick,
            isHomeSelected = isHomeSelected,
            isFavoriteSelected = isFavoriteSelected,
            isProfileSelected = isProfileSelected,
        ) }
    ) { innerPadding ->
        if(mainScreenUiState.value.athleteLoginId == -1) {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                MainScreenTopAppBar(
                    mainScreenViewModel = mainScreenViewModel
                )
                Text(text = "Main Screen! Hello, no user logged in!")
                LazyColumn {
                    items(eventList.value) { item ->
                        EventCard(
                            event = item,
                            isUserLoggedIn = false,
                            onEventClick = { onEventClick(item) }
                            )
                    }
                }
            }
        }
        else {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                MainScreenTopAppBar(
                    mainScreenViewModel = mainScreenViewModel
                )
                Text(text = "Main Screen! Hello ${mainScreenUiState.value.athleteUsername}")
                LazyColumn(
                    modifier = Modifier
                        .imePadding()
                ) {
                    items(eventList.value) { item ->
                        val favoriteIcon = if(item in favoriteEvents) painterResource(R.drawable.baseline_favorite_24)
                            else painterResource(R.drawable.baseline_favorite_border_24)
                        EventCard(
                            event = item,
                            isUserLoggedIn = true,
                            favoriteIcon = favoriteIcon,
                            onFavoriteIconClick = {
                                mainScreenViewModel.onFavoriteIconClick(
                                    athleteId = mainScreenUiState.value.athleteLoginId,
                                    eventId = item.id,
                                    newFavoriteValue = item !in favoriteEvents
                                )
                            },
                            onEventClick = {
                                onEventClick(item)
                            }
                            )
                    }
                }
            }
        }
    }
}

