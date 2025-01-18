package com.example.racebuddy.ui.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.ui.common.BottomAppBar
import com.example.racebuddy.ui.common.EventCard
import com.example.racebuddy.ui.common.LoginTopAppBar
import com.example.racebuddy.ui.main.MainScreenUiState
import com.example.racebuddy.ui.main.MainScreenViewModel

@Composable
fun FavoriteScreen(
    favoriteScreenViewModel: FavoriteScreenViewModel,
    favoriteEventsList: List<Event> = emptyList(),
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    isHomeSelected: Boolean = true,
    isFavoriteSelected: Boolean = false,
    isProfileSelected: Boolean = false,
    onLoginButtonClicked: () -> Unit = {}
) {
    val favoriteScreenUiState by favoriteScreenViewModel.uiState.collectAsState()
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
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if(favoriteScreenUiState.athleteId > 0) {
                LazyColumn() {
                    items(favoriteEventsList) { event ->
                        EventCard(
                            event = event,
                            isUserLoggedIn = true,
                            onFavoriteIconClick = {
                                favoriteScreenViewModel.onFavoriteIconClick(
                                    athleteId = favoriteScreenUiState.athleteId,
                                    eventId = event.id,
                                    newFavoriteValue = false
                                )
                            },
                            favoriteIcon = painterResource(R.drawable.baseline_favorite_24)
                        )
                    }
                }
            }
            else {
                Text("There is no user logged in!")
                Button(
                    onClick = onLoginButtonClicked
                ) {
                    Text("Log In")
                }
            }
        }
    }
}