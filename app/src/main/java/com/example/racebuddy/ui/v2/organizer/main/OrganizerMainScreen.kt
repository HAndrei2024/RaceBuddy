package com.example.racebuddy.ui.v2.organizer.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.cyclingEvents
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.models.Organizer
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.main.Events
import com.example.racebuddy.ui.v2.main.FilterButtons
import com.example.racebuddy.ui.v2.main.HelloText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerMainScreen(
    organizer: OrganizerInfo,
    events: List<EventInfo>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onBottomBarIconClick: () -> Unit,
    selectedFilter: String,
    onFilterButtonClick: (String) -> Unit,
    onEventClick: (EventInfo) -> Unit,
    onFloatingActionButtonClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    val animatedBlur by animateDpAsState(targetValue = if(isRefreshing) 1.dp else 0.dp)

    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSearchIconClick = {},
                onSettingsIconClick = onSettingsIconClick,
                showSearchIcon = false
            )
        },
        bottomBar = {
            BottomNavigationBarChat(
                selectedItem = 0,
                onItemSelected = { onBottomBarIconClick() },
                isFavoritesVisible = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFloatingActionButtonClick,
                containerColor = Color(0xFF4169E1), // Custom background color
                contentColor = Color.White,        // Icon color
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                onRefresh()
            },
        ) {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .blur(
                        radius = animatedBlur,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    //.zIndex(1f)
                    .fillMaxSize()
            ) {
                HelloText(
                    athleteFirstName = organizer.name,
                    imageUrl = "",
                    personalizedText = "Manage your events...",
                    onProfilePicClick = {},
                    modifier = Modifier
                )

                Spacer(
                    modifier = Modifier
                        .padding(paddings.spacingSmall)
                )


                FilterButtons(
                    items = listOf("Upcoming", "Finished"),
                    selectedItem = selectedFilter,
                    onFilterButtonClick = onFilterButtonClick
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = paddings.spacingSmall)
                )

                Events(
                    events = events,
                    favoriteEventsId = emptyList(),
                    onFavoriteIconClick = {string: String, bool: Boolean -> },
                    isUserLoggedIn = false  ,
                    onEventClick = onEventClick
                )

                if(events.isEmpty()) {
                    EmptyListText()
                }

                Spacer(
                    modifier = Modifier
                        .padding(top = paddings.spacingSmall)
                )


            }


        }
    }
}

@Composable
fun EmptyListText(
    text: String = "There are no events...",
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
    ) {
        Text(
            text = text,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun OrganizerMainScreenPreview() {
    OrganizerMainScreen(
        organizer = defaultOrganizer,
        events = cyclingEvents,
        isRefreshing = false,
        onRefresh = { },
        onBottomBarIconClick = {},
        modifier = Modifier,
        selectedFilter = "Upcoming",
        onFilterButtonClick = {},
        onEventClick = {},
        onFloatingActionButtonClick = {},
        onSettingsIconClick = {}
    )
}